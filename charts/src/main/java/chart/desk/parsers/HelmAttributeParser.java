package chart.desk.parsers;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
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

    private ChartEntry getAttributesProvenanceFromInputStream(final InputStream inputStream) throws IOException {
        return provenanceParser.parse(inputStream);
    }

    private ChartEntry getAttributesFromInputStream(final InputStream inputStream) throws IOException {
        try (InputStream is = tgzParser.getChartFromInputStream(inputStream)) {
            return yamlParser.load(is);
        }
    }
}
