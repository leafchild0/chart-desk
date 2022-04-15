package chart.desk.services;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import chart.desk.model.ChartTo;
import chart.desk.model.db.ChartModel;
import chart.desk.repositories.ChartRepository;
import chart.desk.services.storage.StorageService;
import chart.desk.services.storage.StorageType;
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChartService {

    private final ChartRepository chartRepository;
    private final List<StorageService> storageServices;

    /**
     * Store chart in DB and storage.
     *
     * @param chartEntry {@link ChartEntry}
     * @param chart byte array of chart archive
     * @param assetKind {@link AssetKind}
     * @param userId user id
     * @param checkExist boolean flag should we check that this chart already exist
     *
     * @return {@link ChartModel}
     */
    public ChartModel save(ChartEntry chartEntry, byte[] chart, AssetKind assetKind, String userId, boolean checkExist) {
        if (checkExist) {
            Optional<ChartModel> existChart = chartRepository.findChartModelByUserIdAndNameAndVersion(userId, chartEntry.getName(), chartEntry.getVersion());
            if (existChart.isPresent() && Objects.equals(existChart.get().getVersion(), chartEntry.getVersion())) {
                // actual version already exist
                return existChart.get();
            }
        }

        List<String> urls = storageServices.stream()
                .map(s -> s.save(chart, chartEntry.getName(), chartEntry.getVersion(), assetKind, userId))
                .toList();
        String digestHex = DigestUtils.sha256Hex(chart);
        return chartRepository.save(new ChartModel(chartEntry, digestHex, urls, Collections.emptyList(), userId));
    }

    /**
     * Return {@link ChartIndex} with all available charts with all available versions.
     *
     * @param userId user id
     * @return {@link ChartIndex}
     */
    public ChartIndex getIndex(String userId) {
        List<ChartModel> userCharts = chartRepository.findAllByUserId(userId);
        // TODO: handle private charts
        return new ChartIndex(userCharts);
    }

    /**
     * Return list with all available charts with all available versions
     *
     * @param userId user id
     * @return list o ChartTo
     */
    public List<ChartTo> getChartList(String userId) {
        return getIndex(userId).getEntries()
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
                .name(lastVersionChart.getName())
                .versions(chartEntries.stream()
                        .map(ChartEntry::getVersion)
                        .toList())
                .created(lastVersionChart.getCreated())
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

    /**
     * Fetch {@link ChartEntry} from DB if present
     *
     * @param name chart name
     * @param version chart version
     * @param userId user id
     *
     * @return Optional of {@link ChartEntry}
     */
    public Optional<ChartEntry> getChart(String name, String version, String userId) {
        return chartRepository.findChartModelByUserIdAndNameAndVersion(userId, name, version)
                .map(ChartModel::toChartEntry);
    }

    /**
     * Fetch chart archive from local storage
     *
     * @param name chart name
     * @param version chart version
     * @param userId user id
     * @param assetKind {@link AssetKind}
     *
     * @return byte array of chart archive
     */
    public byte[] getChartArchive(String name, String version, AssetKind assetKind, String userId) {
        return storageServices.stream()
                .filter(s -> s.type() == StorageType.LOCAL)
                .map(s -> s.get(name, version, assetKind, userId))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chart not found: " + name + "-" + version));
    }
}
