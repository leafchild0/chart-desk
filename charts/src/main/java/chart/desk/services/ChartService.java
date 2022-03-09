package chart.desk.services;

import chart.desk.model.AssetKind;
import chart.desk.model.HelmAttributes;
import chart.desk.model.db.ChartModel;
import chart.desk.repositories.ChartRepository;
import chart.desk.services.storage.StorageService;
import chart.desk.services.storage.StorageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChartService {

    private final ChartRepository chartRepository;
    private final List<StorageService> storageServices;

    public ChartModel save(HelmAttributes attributes, byte[] chart, AssetKind assetKind, String userId) {
        // TODO: update if exist using chartRepository.findChartModelByNameAndVersionAndUserId(name, version, userId);
        List<String> urls = storageServices.stream()
                .map(s -> s.save(chart, attributes.getName(), attributes.getVersion(), assetKind, userId))
                .collect(Collectors.toList());
        // TODO: calculate digest
        return chartRepository.save(new ChartModel(attributes, null, urls, Collections.emptyList(), userId));
    }

    public byte[] get(String name, String version, AssetKind assetKind, String userId) {
        return storageServices.stream()
                .filter(s -> s.type() == StorageType.LOCAL)
                .map(s -> s.get(name, version, assetKind, userId))
                // TODO: error handling
                .findFirst().orElseThrow(() -> new RuntimeException());
    }
}
