package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartIndex;
import chart.desk.services.ChartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ChartCmdController {

    private final ObjectMapper yamlObjectMapper;
    private final ChartService chartService;

    public ChartCmdController(@Qualifier("yaml_mapper") ObjectMapper yamlObjectMapper, ChartService chartService) {
        this.yamlObjectMapper = yamlObjectMapper;
        this.chartService = chartService;
    }

    // TODO: cache here with caffeine cache etc because creating chart index could take a while
    @GetMapping("{userId}/index.yaml")
    public String getIndexYaml(@PathVariable("userId") String userId) throws JsonProcessingException {
        ChartIndex index = chartService.getIndex(userId);
        return yamlObjectMapper.writeValueAsString(index);
    }

    @GetMapping(value = "/{userId}/{name}-{version}.tgz")
    public ResponseEntity<Mono<byte[]>> getChartArchive(@PathVariable("name") String name, @PathVariable("version") String version, @PathVariable("userId") String userId) {
        byte[] chart = chartService.getChartArchive(name, version, AssetKind.HELM_PACKAGE, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mono.just(chart));
    }

    @GetMapping(value = "/{userId}/{name}-{version}.tgz.prov")
    public ResponseEntity<Mono<byte[]>> getChartProv(@PathVariable("name") String name, @PathVariable("version") String version, @PathVariable("userId") String userId) {
        byte[] chart = chartService.getChartArchive(name, version, AssetKind.HELM_PROVENANCE, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mono.just(chart));
    }
}
