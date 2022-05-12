package chart.desk.parsers;

import chart.desk.errors.ParseException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Service for working with tgz files
 */
@Service
public class TgzParser {
    private static final String CHART_NAME = "Chart.yaml";

    public InputStream getChartFromInputStream(final InputStream is) throws IOException {
        try (GzipCompressorInputStream gzis = new GzipCompressorInputStream(is)) {
            try (TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {
                ArchiveEntry currentEntry;
                while ((currentEntry = tais.getNextEntry()) != null) {
                    if (currentEntry.getName().endsWith(CHART_NAME)) {
                        byte[] buf = new byte[(int) currentEntry.getSize()];
                        tais.read(buf, 0, buf.length);
                        return new ByteArrayInputStream(buf);
                    }
                }
            }
        }
        throw new ParseException(HttpStatus.UNPROCESSABLE_ENTITY, String.format("%s not found", CHART_NAME));
    }
}
