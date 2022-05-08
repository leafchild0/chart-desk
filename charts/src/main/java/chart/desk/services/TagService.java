package chart.desk.services;

import chart.desk.model.db.TagModel;
import chart.desk.model.to.TagTo;
import chart.desk.repositories.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagService {

    private final TagRepository tagRepository;
    private final ChartService chartService;

    public TagModel createTagIfNotExist(String tagName) {
        Optional<TagModel> firstByNameAndColor = tagRepository.findFirstByName(tagName);
        if (firstByNameAndColor.isPresent()) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "Tag already exists");
        }
        return tagRepository.save(new TagModel(tagName));
    }

    public TagModel getTag(Long tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Tag with id " + tagId + " not exists"));
    }

    @Transactional(rollbackFor = Exception.class)
    public void assignTag(TagModel tag, List<String> chartNames, String userName) {
        chartNames.stream()
                .flatMap(chartName -> chartService.getChartList(userName, chartName).stream())
                .map(c -> c.appendTag(tag))
                .forEach(chartService::save);
    }

    @Transactional(rollbackFor = Exception.class)
    public void unassignTag(TagModel tag, List<String> chartNames, String userName) {
        chartNames.stream()
                .flatMap(chartName -> chartService.getChartList(userName, chartName).stream())
                .map(c -> c.removeTag(tag))
                .forEach(chartService::save);
    }

    public List<TagModel> getTags() {
        return tagRepository.findAll();
    }
}
