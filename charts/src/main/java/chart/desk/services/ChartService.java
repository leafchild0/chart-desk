package chart.desk.services;

import chart.desk.model.AssetKind;
import chart.desk.model.ChartEntry;
import chart.desk.model.ChartIndex;
import chart.desk.model.db.ChartModel;
import chart.desk.model.db.SourceModel;
import chart.desk.model.to.ChartTo;
import chart.desk.repositories.ChartRepository;
import chart.desk.services.storage.StorageService;
import chart.desk.services.storage.StorageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
     * @param userName user name
     * @param checkExist boolean flag should we check that this chart already exist
     *
     * @return {@link ChartModel}
     */
    public ChartModel save(ChartEntry chartEntry, byte[] chart, AssetKind assetKind, String userName, SourceModel source, boolean checkExist) {
        if (checkExist) {
            Optional<ChartModel> existChart = chartRepository.findChartModelByUserNameAndNameAndVersion(userName, chartEntry.getName(), chartEntry.getVersion());
            if (existChart.isPresent() && Objects.equals(existChart.get().getVersion(), chartEntry.getVersion())) {
                // actual version already exist
                return existChart.get();
            }
        }

        List<String> urls = storageServices.stream()
                .map(s -> s.save(chart, chartEntry.getName(), chartEntry.getVersion(), assetKind, userName))
                .toList();
        String digestHex = DigestUtils.sha256Hex(chart);
        return chartRepository.save(new ChartModel(chartEntry, digestHex, urls, userName, source));
    }

    /**
     * Return {@link ChartIndex} with all available charts with all available versions.
     *
     * @param userName user name
     * @return {@link ChartIndex}
     */
    public ChartIndex getIndex(String userName) {
        List<ChartModel> userCharts = chartRepository.findAllByUserName(userName);
        // TODO: handle private charts
        return new ChartIndex(userCharts);
    }

    /**
     * Return list with all available charts with all available versions
     *
     * @param userName user name
     * @return list o ChartTo
     */
    public List<ChartTo> getChartList(String userName) {
        return getIndex(userName).toChartsTo();
    }

    /**
     * Fetch {@link ChartEntry} from DB if present
     *
     * @param name chart name
     * @param version chart version
     * @param userName user name
     *
     * @return Optional of {@link ChartEntry}
     */
    public Optional<ChartEntry> getChart(String userName, Long id) {
        return chartRepository.findChartModelByUserNameAndId(userName, id)
                .map(ChartModel::toChartEntry);
    }

    /**
     * Fetch chart archive from local storage
     *
     * @param name chart name
     * @param version chart version
     * @param userName user name
     * @param assetKind {@link AssetKind}
     *
     * @return byte array of chart archive
     */
    public byte[] getChartArchive(String name, String version, AssetKind assetKind, String userName) {
        return storageServices.stream()
                .filter(s -> s.type() == StorageType.LOCAL)
                .map(s -> s.get(name, version, assetKind, userName))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chart not found: " + name + "-" + version));
    }
}
