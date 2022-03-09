package chart.desk.parsers;

import chart.desk.model.AssetKind;
import chart.desk.model.HelmAttributes;
import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class TgzParserTest {
    @Autowired
    private HelmAttributeParser helmAttributeParser;

    @Test
    public void getChartFromInputStream() throws IOException {
        try (InputStream is = Resources.getResource("mariadb-4.2.6.tgz").openStream()) {
            HelmAttributes helmAttributes = helmAttributeParser.getAttributes(AssetKind.HELM_PACKAGE, is);
            Assertions.assertEquals("mariadb", helmAttributes.getName());
            Assertions.assertEquals("4.2.6", helmAttributes.getVersion());
        }
    }
}
