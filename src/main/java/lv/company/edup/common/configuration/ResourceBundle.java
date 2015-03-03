package lv.company.edup.common.configuration;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

public class ResourceBundle {

    private Properties properties = new Properties();

    public ResourceBundle(ResourceProducer producer) {
        Collection<String> resources = producer.getResources();
        for (String resource : resources) {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
            loadProperties(stream);
        }
    }

    protected void loadProperties(InputStream stream) {
        if (stream != null) {
            try {
                properties.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
