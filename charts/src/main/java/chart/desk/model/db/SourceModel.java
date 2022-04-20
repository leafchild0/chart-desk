package chart.desk.model.db;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sources")
@Data
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SourceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "refresh_time")
    private Integer refreshTime;

    public SourceModel(String url, Integer refreshTime) {
        this.url = url;
        this.refreshTime = refreshTime;
    }
}
