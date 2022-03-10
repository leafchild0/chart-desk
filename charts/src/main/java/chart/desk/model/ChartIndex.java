package chart.desk.model;

import chart.desk.model.db.ChartModel;
import chart.desk.util.JodaDateTimeDeserializer;
import chart.desk.util.JodaDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ChartIndex {
    private String apiVersion;
    private Map<String, List<ChartEntry>> entries;
    @JsonSerialize(using = JodaDateTimeSerializer.class)
    @JsonDeserialize(using = JodaDateTimeDeserializer.class)
    private DateTime generated;

    public ChartIndex() {
        this.entries = new HashMap<>();
    }

    public ChartIndex(List<ChartModel> chartEntries) {
        this.apiVersion = "v1";
        this.entries = chartEntries.isEmpty() ? null : chartEntries.stream()
                .map(ChartModel::toChartEntry)
                .collect(Collectors.groupingBy(ChartEntry::getName));
        this.generated = DateTime.now();
    }
}
