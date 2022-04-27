package chart.desk.parsers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class HelmAttributeParser {
    private final TgzParser tgzParser;
    private final YamlParser yamlParser;
    private final ProvenanceParser provenanceParser;

    @Autowired
    public HelmAttributeParser(
            final TgzParser tgzParser,
            final YamlParser yamlParser,
            final ProvenanceParser provenanceParser) {
        this.tgzParser = tgzParser;
        this.yamlParser = yamlParser;
        this.provenanceParser = provenanceParser;
    }

    public ChartEntry getAttributes(final AssetKind assetKind, final InputStream inputStream) throws IOException {
        return switch (assetKind) {
            case HELM_PACKAGE -> getAttributesFromInputStream(inputStream);
            case HELM_PROVENANCE -> getAttributesProvenanceFromInputStream(inputStream);
            default -> new ChartEntry();
        };
    }


    public ChartEntry getHelmAttributes(byte[] chart) {
        ChartEntry chartEntry;
        try (InputStream inputStream = new ByteArrayInputStream(chart)) {
            chartEntry = getAttributes(AssetKind.HELM_PACKAGE, inputStream);
            log.info(chartEntry.toString());
        } catch (IOException e) {
            log.error("Helm attribute parsing failed", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Helm attribute parsing failed", e);
        }
        return chartEntry;
    }

    private ChartEntry getAttributesProvenanceFromInputStream(final InputStream inputStream) throws IOException {
        return provenanceParser.parse(inputStream);
    }

    private ChartEntry getAttributesFromInputStream(final InputStream inputStream) throws IOException {
        try (InputStream is = tgzParser.getChartFromInputStream(inputStream)) {
            return yamlParser.load(is);
        }
    }
}
