package chart.desk.controllers;

import chart.desk.model.db.TagModel;
import chart.desk.model.to.AssignTagTo;
import chart.desk.model.to.TagTo;
import chart.desk.services.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TagsController {

    private final TagService tagService;

    @PostMapping
    public TagModel createTag(@RequestBody TagTo tag) {
        return tagService.createTagIfNotExist(tag.getName());
    }

    @GetMapping
    public List<TagModel> getTags() {
        return tagService.getTags();
    }

    @PostMapping("/{tagId}/assign")
    public void assignTag(@PathVariable Long tagId, @RequestBody AssignTagTo assignTagTo) {
        TagModel tag = tagService.getTag(tagId);
        tagService.assignTag(tag, assignTagTo.getChartNames(), assignTagTo.getUserName());
    }

    @PostMapping("/{tagId}/unassign")
    public void unassignTag(@PathVariable Long tagId, @RequestBody AssignTagTo assignTagTo) {
        TagModel tag = tagService.getTag(tagId);
        tagService.unassignTag(tag, assignTagTo.getChartNames(), assignTagTo.getUserName());
    }
}
