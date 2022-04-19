package chart.desk.model.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyTo {
    private String thirdPartyUrl;
    private List<ChartTo> entries;

    public Map<String, ChartTo> getEntityMap() {
        return getEntries().stream()
                .collect(Collectors.toMap(ChartTo::getName, Function.identity()));
    }
}
