package chart.desk.parsers;

import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Service for getting attributes from yaml files, writing to yaml files
 */
@Service
@Slf4j
public class YamlParser {
    private final ObjectMapper mapper;

    @Autowired
    public YamlParser(@Qualifier("yaml_mapper") ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ChartEntry load(InputStream is) throws IOException {
        return mapper.readValue(is, ChartEntry.class);
    }

    public ChartIndex download(String chartIndex) throws JsonProcessingException {
        return mapper.readValue(chartIndex, ChartIndex.class);
    }
}
