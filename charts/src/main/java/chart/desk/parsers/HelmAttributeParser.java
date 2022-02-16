package chart.desk.parsers;

import chart.desk.model.AssetKind;
import chart.desk.model.HelmAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class HelmAttributeParser
{
  private TgzParser tgzParser;

  private YamlParser yamlParser;

  private ProvenanceParser provenanceParser;

  @Autowired
  public HelmAttributeParser(
      final TgzParser tgzParser,
      final YamlParser yamlParser,
      final ProvenanceParser provenanceParser)
  {
    this.tgzParser = tgzParser;
    this.yamlParser = yamlParser;
    this.provenanceParser = provenanceParser;
  }

  public HelmAttributes getAttributes(final AssetKind assetKind, final InputStream inputStream) throws IOException {
      switch (assetKind) {
        case HELM_PACKAGE:
          return getAttributesFromInputStream(inputStream);
        case HELM_PROVENANCE:
          return getAttributesProvenanceFromInputStream(inputStream);
        default:
          return new HelmAttributes();
      }
  }

  private HelmAttributes getAttributesProvenanceFromInputStream(final InputStream inputStream) throws IOException {
    return provenanceParser.parse(inputStream);
  }

  private HelmAttributes getAttributesFromInputStream(final InputStream inputStream) throws IOException {
    try (InputStream is = tgzParser.getChartFromInputStream(inputStream)) {
      return new HelmAttributes(yamlParser.load(is));
    }
  }
}
