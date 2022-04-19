package chart.desk.model.to;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Builder
public class ProxyTo {
    private String thirdPartyUrl;
    private List<ChartTo> entries;

    public Map<String, ChartTo> getEntityMap() {
        return getEntries().stream()
                .collect(Collectors.toMap(ChartTo::getName, Function.identity()));
    }
}
