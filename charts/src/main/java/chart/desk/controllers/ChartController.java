package chart.desk.controllers;

import chart.desk.model.AssetKind;
import chart.desk.model.HelmAttributes;
import chart.desk.model.db.ChartModel;
import chart.desk.parsers.HelmAttributeParser;
import chart.desk.services.ChartService;
import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Slf4j
public class ChartController {

    private final HelmAttributeParser helmAttributeParser;
    private final ChartService chartService;

    // TODO: just hardcode some index for push helm cmd
    // need to fetch it from database later and maybe cache here with caffeine cache etc
    @GetMapping("/index.yaml")
    public String getIndex() {
        return "apiVersion: v1\n"
                + "entries:\n"
                + "  alpine:\n"
                + "    - created: 2016-10-06T16:23:20.499814565-06:00\n"
                + "      description: Deploy a basic Alpine Linux pod\n"
                + "      digest: 99c76e403d752c84ead610644d4b1c2f2b453a74b921f422b9dcb8a7c8b559cd\n"
                + "      home: https://helm.sh/helm\n"
                + "      name: alpine\n"
                + "      sources:\n"
                + "      - https://github.com/helm/helm\n"
                + "      urls:\n"
                + "      - https://technosophos.github.io/tscharts/alpine-0.2.0.tgz\n"
                + "      version: 0.2.0\n"
                + "    - created: 2016-10-06T16:23:20.499543808-06:00\n"
                + "      description: Deploy a basic Alpine Linux pod\n"
                + "      digest: 515c58e5f79d8b2913a10cb400ebb6fa9c77fe813287afbacf1a0b897cd78727\n"
                + "      home: https://helm.sh/helm\n"
                + "      name: alpine\n"
                + "      sources:\n"
                + "      - https://github.com/helm/helm\n"
                + "      urls:\n"
                + "      - https://technosophos.github.io/tscharts/alpine-0.1.0.tgz\n"
                + "      version: 0.1.0\n"
                + "  nginx:\n"
                + "    - created: 2016-10-06T16:23:20.499543808-06:00\n"
                + "      description: Create a basic nginx HTTP server\n"
                + "      digest: aaff4545f79d8b2913a10cb400ebb6fa9c77fe813287afbacf1a0b897cdffffff\n"
                + "      home: https://helm.sh/helm\n"
                + "      name: nginx\n"
                + "      sources:\n"
                + "      - https://github.com/helm/charts\n"
                + "      urls:\n"
                + "      - https://technosophos.github.io/tscharts/nginx-1.1.0.tgz\n"
                + "      version: 1.1.0\n"
                + "generated: 2016-10-06T16:23:20.499029981-06:00";
    }

    @PostMapping(value = "/api/charts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Mono<ChartModel>> uploadChart(@RequestPart("chart") Flux<FilePart> fileParts) {
        Mono<ChartModel> attributes = toByteArray(fileParts).map(bytes -> {
            HelmAttributes helmAttributes = getHelmAttributes(bytes, AssetKind.HELM_PACKAGE);
            return chartService.save(helmAttributes, bytes, AssetKind.HELM_PACKAGE, getUserId());
        });

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attributes);
    }

    @GetMapping(value = "/{userId}/{name}-{version}.tgz")
    public ResponseEntity<Mono<byte[]>> getChart(@PathVariable("name") String name, @PathVariable("version") String version, @PathVariable("userId") String userId) {
        if (!getUserId().equals(userId)) {
            // TODO: user validation
            throw new RuntimeException();
        }
        byte[] chart = chartService.get(name, version, AssetKind.HELM_PACKAGE, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mono.just(chart));
    }

    @PostMapping("/api/prov")
    public ResponseEntity<Mono<ChartModel>> uploadProvinance(@RequestPart("chart") Flux<FilePart> fileParts) {
        Mono<ChartModel> attributes = toByteArray(fileParts).map(bytes -> {
            HelmAttributes helmAttributes = getHelmAttributes(bytes, AssetKind.HELM_PROVENANCE);
            return chartService.save(helmAttributes, bytes, AssetKind.HELM_PROVENANCE, getUserId());
        });

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attributes);
    }

    private String getUserId() {
        // TODO: integrate with auth
        return "testId";
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

    private HelmAttributes getHelmAttributes(byte[] chart, AssetKind assetKind) {
        HelmAttributes helmAttributes;
        try (InputStream inputStream = new ByteArrayInputStream(chart)) {
            helmAttributes = helmAttributeParser.getAttributes(assetKind, inputStream);
            log.info(helmAttributes.toString());
        } catch (IOException e) {
            // TODO: error handling
            helmAttributes = new HelmAttributes();
            e.printStackTrace();
        }
        return helmAttributes;
    }
}
