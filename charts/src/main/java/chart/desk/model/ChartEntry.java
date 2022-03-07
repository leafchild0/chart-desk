package chart.desk.model;

import chart.desk.util.JodaDateTimeDeserializer;
import chart.desk.util.JodaDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ChartEntry {
    private String description;
    private String name;
    private String version;
    @JsonSerialize(using = JodaDateTimeSerializer.class)
    @JsonDeserialize(using = JodaDateTimeDeserializer.class)
    private DateTime created;
    private String appVersion;
    private String digest;
    private String icon;
    private List<String> urls;
    private List<String> sources;
    private List<Map<String, String>> maintainers;
}
