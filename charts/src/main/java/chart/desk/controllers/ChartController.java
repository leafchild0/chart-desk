package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.db.ChartModel;
import chart.desk.model.to.ChartTo;
import chart.desk.parsers.HelmAttributeParser;
import chart.desk.services.ChartService;
import chart.desk.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Controller for UI
 */
@RestController
@RequestMapping("/{userName}")
@Slf4j
public class ChartController {

    private final HelmAttributeParser helmAttributeParser;
    private final ChartService chartService;

    @Autowired
    public ChartController(HelmAttributeParser helmAttributeParser, ChartService chartService) {
        this.helmAttributeParser = helmAttributeParser;
        this.chartService = chartService;
    }

    @GetMapping
    public List<ChartTo> getChartsList(@PathVariable("userName") String userName) {
        return chartService.getChartList(userName);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Mono<ChartModel>> uploadChart(@RequestPart("chart") Flux<FilePart> fileParts, @PathVariable("userName") String userName) {
        Mono<ChartModel> attributes = FileUtils.toByteArray(fileParts).map(bytes -> {
            ChartEntry helmAttributes = helmAttributeParser.getHelmAttributes(bytes);
            return chartService.save(helmAttributes, bytes, AssetKind.HELM_PACKAGE, userName, null,true);
        });

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attributes);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Mono<ChartEntry>> getChart(@PathVariable("id") Long id, @PathVariable("userName") String userName) {

        Optional<ChartEntry> chart = chartService.getChart(userName, id);
        return chart.map(entry -> ResponseEntity.status(HttpStatus.OK).body(Mono.just(entry)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Mono.empty()));
    }

    @GetMapping(value = "/{name}/versions")
    public ResponseEntity<Mono<List<Pair<Long, String>>>> getChart(@PathVariable("name") String name, @PathVariable("userName") String userName) {

        return ResponseEntity.status(HttpStatus.OK).body(Mono.just(chartService.getChartList(userName, name)
            .stream().map(chart -> Pair.of(chart.getId(), chart.getVersion())).toList()));

    }

}
