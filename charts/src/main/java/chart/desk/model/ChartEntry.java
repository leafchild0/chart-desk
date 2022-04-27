package chart.desk.model;

import chart.desk.model.to.TagTo;
import chart.desk.util.JodaDateTimeDeserializer;
import chart.desk.util.JodaDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartEntry {
    private String description;
    private Long id;
    private String name;
    private String version;
    @JsonSerialize(using = JodaDateTimeSerializer.class)
    @JsonDeserialize(using = JodaDateTimeDeserializer.class)
    private DateTime created;
    private String appVersion;
    private String digest;
    private String icon;
    private String engine;
    private String home;
    private List<String> urls;
    private List<String> sources;
    private List<Map<String, String>> maintainers;
    private List<String> keywords;
    private List<TagTo> tags = Collections.emptyList();
}
