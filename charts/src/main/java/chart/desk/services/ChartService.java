package chart.desk.services;

import chart.desk.model.HelmAttributes;
import chart.desk.model.db.ChartModel;
import chart.desk.repositories.ChartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChartService {

    private final ChartRepository chartRepository;

    public ChartModel save(HelmAttributes attributes) {
        // TODO: store chart to s3 and set urls as second param
        // TODO: calculate digest
        return chartRepository.save(new ChartModel(attributes, null, Collections.emptyList()));
    }
}
