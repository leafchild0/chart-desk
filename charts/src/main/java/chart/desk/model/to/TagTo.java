package chart.desk.model.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagTo {
    private Long id;
    private String name;
    private boolean isCustom;
    private LocalDate created;

    public TagTo(Long id, String name, boolean isCustom) {
        this.id = id;
        this.name = name;
        this.isCustom = isCustom;
        this.created = new LocalDate();
    }
}
