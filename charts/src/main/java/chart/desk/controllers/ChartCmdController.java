package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import chart.desk.model.db.ChartModel;
import chart.desk.parsers.HelmAttributeParser;
import chart.desk.services.ChartService;
import chart.desk.util.FileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for all CMD operations using helm
 */
@RestController
@Slf4j
public class ChartCmdController {

    private final ObjectMapper yamlObjectMapper;
    private final HelmAttributeParser helmAttributeParser;
    private final ChartService chartService;

    public ChartCmdController(@Qualifier("yaml_mapper") ObjectMapper yamlObjectMapper, HelmAttributeParser helmAttributeParser, ChartService chartService) {
        this.yamlObjectMapper = yamlObjectMapper;
        this.helmAttributeParser = helmAttributeParser;
        this.chartService = chartService;
    }

    // TODO: cache here with caffeine cache etc because creating chart index could take a while
    @GetMapping("{userName}/index.yaml")
    public String getIndexYaml(@PathVariable("userName") String userName) throws JsonProcessingException {
        ChartIndex index = chartService.getIndex(userName);
        return yamlObjectMapper.writeValueAsString(index);
    }

    @GetMapping(value = "/{userName}/{name}-{version}.tgz")
    public ResponseEntity<Mono<byte[]>> getChartArchive(@PathVariable("name") String name, @PathVariable("version") String version, @PathVariable("userName") String userName) {
        byte[] chart = chartService.getChartArchive(name, version, AssetKind.HELM_PACKAGE, userName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mono.just(chart));
    }

    @PostMapping(value = "/api/{userName}/charts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Mono<ChartModel>> uploadChart(@RequestPart("chart") Flux<FilePart> fileParts, @PathVariable("userName") String userName) {
        Mono<ChartModel> attributes = FileUtils.toByteArray(fileParts).map(bytes -> {
            ChartEntry helmAttributes = helmAttributeParser.getHelmAttributes(bytes);
            return chartService.save(helmAttributes, bytes, AssetKind.HELM_PACKAGE, userName, null,true);
        });

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(attributes);
    }
}
