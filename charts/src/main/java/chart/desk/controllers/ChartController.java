package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import chart.desk.model.db.ChartModel;
import chart.desk.parsers.HelmAttributeParser;
import chart.desk.services.ChartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

@RestController
@Slf4j
public class ChartController {

    private final HelmAttributeParser helmAttributeParser;
    private final ChartService chartService;
    private final ObjectMapper yamlObjectMapper;
    private final ObjectMapper jsonObjectMapper;

    @Autowired
    public ChartController(HelmAttributeParser helmAttributeParser,
            ChartService chartService,
            @Qualifier("yaml_mapper") ObjectMapper yamlObjectMapper,
            @Qualifier("json_mapper") ObjectMapper jsonObjectMapper) {
        this.helmAttributeParser = helmAttributeParser;
        this.chartService = chartService;
        this.yamlObjectMapper = yamlObjectMapper;
        this.jsonObjectMapper = jsonObjectMapper;
    }

    // TODO: need to fetch it from database later and maybe cache here with caffeine cache etc
    @GetMapping("{userId}/index.yaml")
    public String getIndexYaml(@PathVariable("userId") String userId) throws JsonProcessingException {
        ChartIndex index = chartService.getIndex(userId);
        return yamlObjectMapper.writeValueAsString(index);
    }

    @GetMapping("{userId}/index.json")
    public String getIndexJson(@PathVariable("userId") String userId) throws JsonProcessingException {
        ChartIndex index = chartService.getIndex(userId);
        return jsonObjectMapper.writeValueAsString(index);
    }

    @PostMapping(value = "/api/{userId}/charts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Mono<ChartModel>> uploadChart(@RequestPart("chart") Flux<FilePart> fileParts, @PathVariable("userId") String userId) {
        Mono<ChartModel> attributes = toByteArray(fileParts).map(bytes -> {
            ChartEntry helmAttributes = getHelmAttributes(bytes, AssetKind.HELM_PACKAGE);
            return chartService.save(helmAttributes, bytes, AssetKind.HELM_PACKAGE, userId);
        });

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attributes);
    }

    @GetMapping(value = "/{userId}/{name}-{version}.tgz")
    public ResponseEntity<Mono<byte[]>> getChart(@PathVariable("name") String name, @PathVariable("version") String version, @PathVariable("userId") String userId) {
        byte[] chart = chartService.get(name, version, AssetKind.HELM_PACKAGE, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mono.just(chart));
    }

    @PostMapping("{userId}/api/prov")
    public ResponseEntity<Mono<ChartModel>> uploadProvinance(@RequestPart("chart") Flux<FilePart> fileParts, @PathVariable("userId") String userId) {
        Mono<ChartModel> attributes = toByteArray(fileParts).map(bytes -> {
            ChartEntry helmAttributes = getHelmAttributes(bytes, AssetKind.HELM_PROVENANCE);
            return chartService.save(helmAttributes, bytes, AssetKind.HELM_PROVENANCE, userId);
        });

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attributes);
    }

    private Mono<byte[]> toByteArray(Flux<FilePart> fileParts) {
        return fileParts
                .flatMap(Part::content)
                .map(DataBuffer::asInputStream)
                .reduce(SequenceInputStream::new)
                .map(this::toByteArray);
    }

    @SneakyThrows
    private byte[] toByteArray(InputStream inputStream) {
        return ByteStreams.toByteArray(inputStream);
    }

    private ChartEntry getHelmAttributes(byte[] chart, AssetKind assetKind) {
        ChartEntry chartEntry;
        try (InputStream inputStream = new ByteArrayInputStream(chart)) {
            chartEntry = helmAttributeParser.getAttributes(assetKind, inputStream);
            log.info(chartEntry.toString());
        } catch (IOException e) {
            // TODO: error handling
            chartEntry = new ChartEntry();
            e.printStackTrace();
        }
        return chartEntry;
    }
}
