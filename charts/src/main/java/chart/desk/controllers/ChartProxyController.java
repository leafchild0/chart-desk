package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartIndex;
import chart.desk.parsers.YamlParser;
import chart.desk.services.ChartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vdurmont.semver4j.Semver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;

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
    public void proxyIndex(@PathVariable("userId") String userId,
            @RequestParam("thirdPartyUrl") String thirdPartyUrl) throws JsonProcessingException {
        String chartIndex = restTemplate.getForObject(thirdPartyUrl, String.class);
        ChartIndex index = yamlParser.download(chartIndex);
        index.getEntries().values().forEach(charts -> charts.stream()
                .forEach(chartEntry -> {
                    log.info("Saving chart {} {}", chartEntry.getName(), chartEntry.getVersion());
                    byte[] chart = downloadFirstChart(chartEntry.getUrls());
                    if (chart.length == 0) {
                        log.error("Downloading chart failed: {} {}", chartEntry.getName(), chartEntry.getVersion());
                        return;
                    }
                    try {
                        chartService.save(chartEntry, chart, AssetKind.HELM_PACKAGE, userId);
                    } catch (Exception e) {
                        log.error("Saving chart failed: {} {}", chartEntry.getName(), chartEntry.getVersion());
                    }
                }));
    }

    private byte[] downloadFirstChart(List<String> urls) {
        for (String url : urls) {
            try {
                return restTemplate.getForObject(url, byte[].class);
            } catch (Exception e) {
                log.warn("Download chart from {} failed.", url, e);
            }
        }
        return new byte[] {};
    }
}

