package chart.desk.parsers;

import chart.desk.controllers.ChartController;
import chart.desk.model.AssetKind;
import chart.desk.model.HelmAttributes;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
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
