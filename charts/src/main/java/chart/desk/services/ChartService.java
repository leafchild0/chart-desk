package chart.desk.services;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import chart.desk.model.db.ChartModel;
import chart.desk.repositories.ChartRepository;
import chart.desk.services.storage.StorageService;
import chart.desk.services.storage.StorageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChartService {

    private final ChartRepository chartRepository;
    private final List<StorageService> storageServices;

    public ChartModel save(ChartEntry chartEntry, byte[] chart, AssetKind assetKind, String userId) {
        Optional<ChartModel> existChart = chartRepository.findChartModelByNameAndUserId(chartEntry.getName(), userId);

        if (existChart.isPresent() && Objects.equals(existChart.get().getVersion(), chartEntry.getVersion())) {
            // actual version already exist
            return existChart.get();
        }

        List<String> urls = storageServices.stream()
                .map(s -> s.save(chart, chartEntry.getName(), chartEntry.getVersion(), assetKind, userId))
                .collect(Collectors.toList());
        // update or create new one
        ChartModel newChartModel = existChart.map(model -> new ChartModel(model.getId(), chartEntry, null, urls, Collections.emptyList(), userId))
                .orElse(new ChartModel(chartEntry, null, urls, Collections.emptyList(), userId));
        return chartRepository.save(newChartModel);
    }

    public ChartIndex getIndex(String userId) {
        List<ChartModel> userCharts = chartRepository.findAllByUserId(userId);
        // TODO: handle private charts
        return new ChartIndex(userCharts);
    }

    public byte[] get(String name, String version, AssetKind assetKind, String userId) {
        return storageServices.stream()
                .filter(s -> s.type() == StorageType.LOCAL)
                .map(s -> s.get(name, version, assetKind, userId))
                // TODO: error handling
                .findFirst().orElseThrow(() -> new RuntimeException());
    }
}
