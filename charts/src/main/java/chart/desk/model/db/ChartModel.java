package chart.desk.model.db;

import chart.desk.model.ChartEntry;
import chart.desk.model.to.TagTo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "charts")
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ChartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "version", nullable = false)
    private String version;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "app_version")
    private String appVersion;

    @Column(name = "digest")
    private String digest;

    @Column(name = "engine")
    private String engine;

    @NotNull
    @Column(name = "icon")
    private String icon;

    @NotNull
    @Column(name = "home")
    private String home;

    @NotNull
    @Column(name = "keywords")
    private String keywords;

    @NotNull
    @Column(name = "urls", nullable = false)
    private String urls;

    @NotNull
    @Column(name = "sources")
    private String sources;

    @NotNull
    @Column(name = "maintainers")
    private String maintainers;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "created", nullable = false)
    private Date created;

    @ManyToOne
    @JoinColumn(name="source_id")
    private SourceModel source;

    @ManyToMany
    Set<TagModel> tags;

    @SneakyThrows
    public ChartModel(ChartEntry chartEntry, String digest, List<String> urls, String userName, SourceModel source) {
        this.name = chartEntry.getName();
        this.version = chartEntry.getVersion();
        this.description = chartEntry.getDescription();
        this.appVersion = chartEntry.getAppVersion();
        this.icon = chartEntry.getIcon();
        this.engine = chartEntry.getEngine();
        this.home = chartEntry.getHome();
        ObjectMapper objectMapper = new ObjectMapper();
        this.keywords = objectMapper.writeValueAsString(chartEntry.getKeywords());
        this.sources = objectMapper.writeValueAsString(chartEntry.getSources());
        this.maintainers = objectMapper.writeValueAsString(chartEntry.getMaintainers());
        this.urls = objectMapper.writeValueAsString(urls);
        this.created = new Date();
        this.digest = digest;
        this.userName = userName;
        this.source = source;
    }

    @SneakyThrows
    public ChartModel(Long id, ChartEntry chartEntry, String digest, List<String> urls, String userName) {
        this.id = id;
        this.name = chartEntry.getName();
        this.version = chartEntry.getVersion();
        this.description = chartEntry.getDescription();
        this.appVersion = chartEntry.getAppVersion();
        this.icon = chartEntry.getIcon();
        this.engine = chartEntry.getEngine();
        this.home = chartEntry.getHome();
        ObjectMapper objectMapper = new ObjectMapper();
        this.keywords = objectMapper.writeValueAsString(chartEntry.getKeywords());
        this.sources = objectMapper.writeValueAsString(chartEntry.getSources());
        this.maintainers = objectMapper.writeValueAsString(chartEntry.getMaintainers());
        this.urls = objectMapper.writeValueAsString(urls);
        this.created = new Date();
        this.digest = digest;
        this.userName = userName;
    }

    @SneakyThrows
    public ChartEntry toChartEntry() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> urlsList = objectMapper.readValue(getUrls(), List.class);
        List<String> sourcesList = objectMapper.readValue(getSources(), List.class);
        List<String> keywordsList = objectMapper.readValue(getKeywords(), List.class);
        List<Map<String, String>> maintainersList = objectMapper.readValue(getMaintainers(), List.class);
        List<TagTo> customTags = getTags().stream().map(a-> new TagTo(a.getName(), true)).toList();
        List<TagTo> helmTags = keywordsList.stream().map(a-> new TagTo(a, false)).toList();
        return ChartEntry.builder()
                .id(getId())
                .description(getDescription())
                .name(getName())
                .version(getVersion())
                .created(new DateTime(getCreated().getTime()))
                .appVersion(getAppVersion())
                .digest(getDigest())
                .icon(getIcon())
                .urls(urlsList)
                .sources(sourcesList)
                .maintainers(maintainersList)
                .engine(getEngine())
                .home(getHome())
                .keywords(keywordsList)
                .tags(Stream.concat(customTags.stream(), helmTags.stream()).toList())
                .build();
    }

    public ChartModel appendTag(TagModel tag) {
        Set<TagModel> tagSet = getTags();
        tagSet.add(tag);
        setTags(tagSet);
        return this;
    }
}
