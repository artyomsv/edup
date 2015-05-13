package lv.company.edup.infrastructure.templates.impl;

import lv.company.edup.infrastructure.templates.api.VelocityProperties;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.log.NullLogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.enterprise.inject.Produces;
import java.util.Properties;

public class VelocityPropertiesProducer {

    @Produces
    @VelocityProperties
    public Properties getVelocityProperties() {
        Properties properties = new Properties();
        properties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        properties.setProperty("class." + RuntimeConstants.RESOURCE_LOADER + ".class", ClasspathResourceLoader.class.getName());
        properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, NullLogChute.class.getName());
        return properties;
    }

}
