package chart.desk.parsers;

import chart.desk.model.HelmAttributes;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TgzParserTest {
    @Autowired
    private HelmAttributeParser helmAttributeParser;
//    @Autowired
//    private YamlParser yamlParser;

    @Test
    public void getChartFromInputStream() throws IOException {
        InputStream is = Resources.getResource("mariadb-4.2.6.tgz").openStream();
            HelmAttributes helmAttributes = helmAttributeParser.getAttributesFromInputStream(is);
            System.out.println(helmAttributes.toString());
    }
}
