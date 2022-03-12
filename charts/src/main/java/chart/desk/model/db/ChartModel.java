package chart.desk.model.db;

import chart.desk.model.ChartEntry;
import chart.desk.model.HelmAttributes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Audited
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
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "app_version", nullable = false)
    private String appVersion;

    @Column(name = "digest", nullable = false)
    private String digest;

    @Column(name = "engine", nullable = false)
    private String engine;

    @NotNull
    @Column(name = "icon", nullable = false)
    private String icon;

    @NotNull
    @Column(name = "home", nullable = false)
    private String home;

    @NotNull
    @Column(name = "keywords", nullable = false)
    private String keywords;

    @NotNull
    @Column(name = "urls", nullable = false)
    private String urls;

    @NotNull
    @Column(name = "prov_urls", nullable = false)
    private String provUrls;

    @NotNull
    @Column(name = "sources", nullable = false)
    private String sources;

    @NotNull
    @Column(name = "maintainers", nullable = false)
    private String maintainers;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private String userId;

    @NotNull
    @Column(name = "created", nullable = false)
    private Date created;

    @SneakyThrows
    public ChartModel(HelmAttributes attributes, String digest, List<String> urls, List<String> provUrls, String userId) {
        this.name = attributes.getName();
        this.version = attributes.getVersion();
        this.description = attributes.getDescription();
        this.appVersion = attributes.getAppVersion();
        this.icon = attributes.getIcon();
        this.engine = attributes.getEngine();
        this.home = attributes.getHome();
        ObjectMapper objectMapper = new ObjectMapper();
        this.keywords = objectMapper.writeValueAsString(attributes.getKeywords());
        this.sources = objectMapper.writeValueAsString(attributes.getSources());
        this.maintainers = objectMapper.writeValueAsString(attributes.getMaintainers());
        this.urls = objectMapper.writeValueAsString(urls);
        this.provUrls = objectMapper.writeValueAsString(provUrls);
        this.created = new Date();
        this.digest = digest;
        this.userId = userId;
    }

    @SneakyThrows
    public ChartModel(Long id, HelmAttributes attributes, String digest, List<String> urls, List<String> provUrls, String userId) {
        this.id = id;
        this.name = attributes.getName();
        this.version = attributes.getVersion();
        this.description = attributes.getDescription();
        this.appVersion = attributes.getAppVersion();
        this.icon = attributes.getIcon();
        this.engine = attributes.getEngine();
        this.home = attributes.getHome();
        ObjectMapper objectMapper = new ObjectMapper();
        this.keywords = objectMapper.writeValueAsString(attributes.getKeywords());
        this.sources = objectMapper.writeValueAsString(attributes.getSources());
        this.maintainers = objectMapper.writeValueAsString(attributes.getMaintainers());
        this.urls = objectMapper.writeValueAsString(urls);
        this.provUrls = objectMapper.writeValueAsString(provUrls);
        this.created = new Date();
        this.digest = digest;
        this.userId = userId;
    }

    @SneakyThrows
    public ChartEntry toChartEntry() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> urls = objectMapper.readValue(getUrls(), List.class);
        List<String> sources = objectMapper.readValue(getSources(), List.class);
        List<Map<String, String>> maintainers = objectMapper.readValue(getMaintainers(), List.class);
        return ChartEntry.builder()
                .description(getDescription())
                .name(getName())
                .version(getVersion())
                .created(new DateTime(getCreated().getTime()))
                .appVersion(getAppVersion())
                .digest(getDigest())
                .icon(getIcon())
                .urls(urls)
                .sources(sources)
                .maintainers(maintainers)
                .build();
    }
}
