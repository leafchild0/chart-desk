package chart.desk.parsers;

import chart.desk.model.ChartEntry;
import chart.desk.model.HelmProperties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class ProvenanceParser {
    public ChartEntry parse(final InputStream inputStream) throws IOException {
        ChartEntry chartEntry = new ChartEntry();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith(HelmProperties.NAME.getPropertyName())) {
                    chartEntry.setName(getValue(line));
                }
                if (line.startsWith(HelmProperties.DESCRIPTION.getPropertyName())) {
                    chartEntry.setDescription(getValue(line));
                }
                if (line.startsWith(HelmProperties.VERSION.getPropertyName())) {
                    chartEntry.setVersion(getValue(line));
                }
                if (line.startsWith(HelmProperties.ICON.getPropertyName())) {
                    chartEntry.setIcon(getValue(line));
                }
                if (line.startsWith(HelmProperties.APP_VERSION.getPropertyName())) {
                    chartEntry.setAppVersion(getValue(line));
                }
            }
        }
        return chartEntry;
    }

    private String getValue(String string) {
        return string.substring(string.indexOf(":") + 1).trim();
    }
}
