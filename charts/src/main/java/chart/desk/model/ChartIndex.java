package chart.desk.model;

import chart.desk.model.db.ChartModel;
import chart.desk.model.to.ChartTo;
import chart.desk.util.JodaDateTimeDeserializer;
import chart.desk.util.JodaDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        this.entries = chartEntries.isEmpty() ? Collections.emptyMap() : chartEntries.stream()
                .map(ChartModel::toChartEntry)
                .collect(Collectors.groupingBy(ChartEntry::getName));
        this.generated = DateTime.now();
    }

    public List<ChartTo> toChartsTo() {
        return getEntries()
                .values().stream()
                .map(this::toChartTo)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ChartTo> toChartTo(List<ChartEntry> charts) {
        List<ChartEntry> chartEntries = charts.stream()
                .sorted(Comparator.comparing(a -> normalizeVersion(a.getVersion())))
                .toList();

        if (chartEntries.isEmpty()) {
            return Optional.empty();
        }

        ChartEntry lastVersionChart = chartEntries.get(chartEntries.size() - 1);
        return Optional.of(ChartTo.builder()
                .id(lastVersionChart.getId())
                .name(lastVersionChart.getName())
                .versions(chartEntries.stream()
                        .map(ChartEntry::getVersion)
                        .toList())
                .created(lastVersionChart.getCreated().toLocalDate())
                .tags(lastVersionChart.getKeywords())
                .build());
    }

    private Semver normalizeVersion(String version) {
        try {
            return new Semver(version);
        } catch (SemverException e) {
            // v0.0.1 -> 0.0.1
            if (version.startsWith("v")) {
                return normalizeVersion(version.substring(1));
            }

            // put chart in the end of charts list if version could not be parsed.
            return new Semver("0.0.0");
        }
    }
}
