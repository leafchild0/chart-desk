package chart.desk.services;

import chart.desk.model.db.SourceModel;
import chart.desk.repositories.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SourceService {

    private static final int DEFAULT_REFRESH_TIME = 7;

    private final SourceRepository sourceRepository;

    public SourceModel saveIfNotExist(String url) {
        return sourceRepository.findFirstByUrl(url)
                .orElse(sourceRepository.save(new SourceModel(url, DEFAULT_REFRESH_TIME)));
    }
}
