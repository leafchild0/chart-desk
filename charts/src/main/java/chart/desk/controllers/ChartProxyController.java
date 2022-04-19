package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import chart.desk.model.db.SourceModel;
import chart.desk.model.to.ChartTo;
import chart.desk.model.to.ProxyTo;
import chart.desk.parsers.YamlParser;
import chart.desk.services.ChartService;
import chart.desk.services.SourceService;
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
    private final SourceService sourceService;
    private final YamlParser yamlParser;
    private final RestTemplate restTemplate;

    @Autowired
    public ChartProxyController(ChartService chartService, SourceService sourceService,
            YamlParser yamlParser, RestTemplate restTemplate) {
        this.chartService = chartService;
        this.sourceService = sourceService;
        this.yamlParser = yamlParser;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/proxy/convert")
    public List<ChartTo> convertIndex(@RequestBody ProxyTo body) throws JsonProcessingException, MalformedURLException, URISyntaxException {
        String thirdPartyUrl = body.getThirdPartyUrl();
        String chartIndex = restTemplate.getForObject(new URL(thirdPartyUrl).toURI(), String.class);
        return yamlParser.download(chartIndex).toChartsTo();
    }

    @PostMapping("/proxy/{userId}")
    public Map<Boolean, Long> proxyIndex(@PathVariable("userId") String userId,
            @RequestBody ProxyTo body) throws JsonProcessingException, MalformedURLException, URISyntaxException {
        String thirdPartyUrl = body.getThirdPartyUrl();
        SourceModel source = sourceService.saveIfNotExist(thirdPartyUrl);

        Map<String, ChartTo> selectedChartEntries = body.getEntityMap();
        Map<String, List<ChartEntry>> existedCharts = chartService.getIndex(userId).getEntries();

        String chartIndex = restTemplate.getForObject(new URL(source.getUrl()).toURI(), String.class);
        ChartIndex index = yamlParser.download(chartIndex);

        try (PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(connectionManager).build()) {
            return index.getEntries().values().stream()
                    .flatMap(Collection::stream)
                    .parallel()
                    .filter(a -> selectedChartEntries.containsKey(a.getName()) && selectedChartEntries.get(a.getName()).getVersions().contains(a.getVersion()))
                    .filter(a -> existedCharts.getOrDefault(a.getName(), Collections.emptyList()).stream().noneMatch(b -> Objects.equals(a.getVersion(), b.getVersion())))
                    .map(chartEntry -> {
                        log.info("Saving chart {} {}", chartEntry.getName(), chartEntry.getVersion());
                        byte[] chart = downloadFirstChart(chartEntry.getUrls(), client);
                        if (chart.length == 0) {
                            log.error("Downloading chart failed: {} {}", chartEntry.getName(), chartEntry.getVersion());
                            return false;
                        }
                        try {
                            chartService.save(chartEntry, chart, AssetKind.HELM_PACKAGE, userId, source,false);
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
