package chart.desk.parsers;

import chart.desk.model.ChartIndex;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.util.Map;

/**
 * Service for getting attributes from yaml files, writing to yaml files
 */
@Service
public class YamlParser
{
  private final ObjectMapper mapper;

  @Autowired
  public YamlParser(@Qualifier("yaml_mapper") ObjectMapper mapper) {
    this.mapper = mapper;
  }

  private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE
          = new TypeReference<>() {
  };

  public Map<String, Object> load(InputStream is) throws IOException {
    return mapper.readValue(is, MAP_TYPE_REFERENCE);
  }

  public String getYamlContent(final ChartIndex index) {
    try {
      return mapper.writeValueAsString(index);
    }
    catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public void write(final OutputStream os, final ChartIndex index) {
    try (OutputStreamWriter writer = new OutputStreamWriter(os)) {
      String result = getYamlContent(index);
      writer.write(result);
    }
    catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
}