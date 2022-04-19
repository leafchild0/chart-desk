package chart.desk.model.to;

import chart.desk.util.JodaDateTimeDeserializer;
import chart.desk.util.JodaDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.List;

@Data
@Builder
public class ChartTo {
    private String name;
    private List<String> versions;
    @JsonSerialize(using = JodaDateTimeSerializer.class)
    @JsonDeserialize(using = JodaDateTimeDeserializer.class)
    private DateTime created;
    private List<String> tags;
}
