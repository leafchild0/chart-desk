package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.db.ChartModel;
import chart.desk.model.to.ChartTo;
import chart.desk.parsers.HelmAttributeParser;
import chart.desk.services.ChartService;
import com.google.common.io.ByteStreams;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/{userName}/charts")
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
        Mono<ChartModel> attributes = toByteArray(fileParts).map(bytes -> {
            ChartEntry helmAttributes = getHelmAttributes(bytes);
            return chartService.save(helmAttributes, bytes, AssetKind.HELM_PACKAGE, userName, null,true);
        });

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attributes);
    }

    @GetMapping(value = "/{name}/{version}")
    public ResponseEntity<Mono<ChartEntry>> getChart(@PathVariable("name") String name, @PathVariable("version") String version, @PathVariable("userName") String userName) {
        Optional<ChartEntry> chart = chartService.getChart(name, version, userName);
        return chart.map(entry -> ResponseEntity.status(HttpStatus.OK).body(Mono.just(entry)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Mono.empty()));
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

    private ChartEntry getHelmAttributes(byte[] chart) {
        ChartEntry chartEntry;
        try (InputStream inputStream = new ByteArrayInputStream(chart)) {
            chartEntry = helmAttributeParser.getAttributes(AssetKind.HELM_PACKAGE, inputStream);
            log.info(chartEntry.toString());
        } catch (IOException e) {
            log.error("Helm attribute parsing failed", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Helm attribute parsing failed", e);
        }
        return chartEntry;
    }
}
