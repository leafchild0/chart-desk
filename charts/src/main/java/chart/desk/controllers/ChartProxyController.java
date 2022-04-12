package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import chart.desk.parsers.YamlParser;
import chart.desk.services.ChartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Slf4j
public class ChartProxyController {

    private final ChartService chartService;
    private final YamlParser yamlParser;
    private final RestTemplate restTemplate;

    @Autowired
    public ChartProxyController(ChartService chartService, YamlParser yamlParser, RestTemplate restTemplate) {
        this.chartService = chartService;
        this.yamlParser = yamlParser;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/proxy/{userId}")
    public Map<Boolean, Long> proxyIndex(@PathVariable("userId") String userId,
            @RequestBody Map<String, String> body) throws JsonProcessingException, MalformedURLException, URISyntaxException {
        String thirdPartyUrl = body.get("thirdPartyUrl");
        // Creating new URL and perform validation checks
        String chartIndex = restTemplate.getForObject(new URL(thirdPartyUrl).toURI(), String.class);
        ChartIndex index = yamlParser.download(chartIndex);
        Map<String, List<ChartEntry>> existedCharts = chartService.getIndex(userId).getEntries();
        try (PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager).build()) {
            return index.getEntries().values().stream()
                    .flatMap(Collection::stream)
                    .parallel()
                    .filter(a -> existedCharts.getOrDefault(a.getName(), Collections.emptyList()).stream().noneMatch(b -> Objects.equals(a.getVersion(), b.getVersion())))
                    .map(chartEntry -> {
                        log.info("Saving chart {} {}", chartEntry.getName(), chartEntry.getVersion());
                        byte[] chart = downloadFirstChart(chartEntry.getUrls(), client);
                        if (chart.length == 0) {
                            log.error("Downloading chart failed: {} {}", chartEntry.getName(), chartEntry.getVersion());
                            return false;
                        }
                        try {
                            chartService.save(chartEntry, chart, AssetKind.HELM_PACKAGE, userId, false);
                            return true;
                        } catch (Exception e) {
                            log.error("Saving chart failed: {} {}", chartEntry.getName(), chartEntry.getVersion(), e);
                            return false;
                        }
                    }).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        } catch (Exception e) {
            log.error("Proxy helm repository {} failed.", thirdPartyUrl, e);
            throw new ResponseStatusException(BAD_REQUEST, "Proxy helm repository " + thirdPartyUrl + " failed.", e);
        }
    }

    private byte[] downloadFirstChart(List<String> urls, CloseableHttpClient client) {
        for (String url : urls) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse execute = client.execute(request)) {
                if (execute.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    try (InputStream is = execute.getEntity().getContent()) {
                        return is.readAllBytes();
                    }
                }
            } catch (IOException e) {
                log.warn("Download chart from {} failed.", url, e);
            }
        }
        return new byte[] {};
    }
}
