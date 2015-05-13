package lv.company.edup.infrastructure.templates.impl;

import lv.company.edup.infrastructure.templates.api.Template;
import lv.company.edup.infrastructure.templates.api.TemplateEngine;
import lv.company.edup.infrastructure.templates.api.VelocityEngine;
import lv.company.edup.infrastructure.templates.api.VelocityProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@VelocityEngine
@ApplicationScoped
public class VelocityTemplateEngine implements TemplateEngine {

    @Inject @VelocityProperties Properties properties;
    private Logger logger = Logger.getLogger(VelocityTemplateEngine.class.getSimpleName());

    @PostConstruct
    public void init() {
        try {
            Velocity.init(properties);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    @Override
    public String render(Template template, Map<String, Object> objects) {
        VelocityContext context = new VelocityContext();

        for (Map.Entry<String, Object> entry : objects.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            context.put(entry.getKey(), entry.getValue());
        }

        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, template.getName().name(), template.getTemplate());
        return writer.toString();

    }

}
