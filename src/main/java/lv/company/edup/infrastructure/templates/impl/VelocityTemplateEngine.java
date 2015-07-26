package lv.company.edup.infrastructure.templates.impl;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.templates.api.Template;
import lv.company.edup.infrastructure.templates.api.TemplateEngine;
import lv.company.edup.infrastructure.templates.api.Type;
import lv.company.edup.infrastructure.templates.api.VelocityEngine;
import lv.company.edup.infrastructure.templates.api.VelocityProperties;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@VelocityEngine
@ApplicationScoped
public class VelocityTemplateEngine implements TemplateEngine {

    private Properties properties;
    private Logger logger = Logger.getLogger(VelocityTemplateEngine.class.getSimpleName());

    @PostConstruct
    @Override
    public void init() {
        try {
            Velocity.init(properties);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    @Override
    public byte[] render(byte[] template, Map<String, Object> objects, Type type) {
        if (type != Type.HTML) {
            throw new InternalException("Velocity supports only html rendering");
        }
        VelocityContext context = new VelocityContext();

        for (Map.Entry<String, Object> entry : objects.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            context.put(entry.getKey(), entry.getValue());
        }

        StringWriter writer = new StringWriter();
        try {
            Velocity.evaluate(context, writer, "velocity_template_engine", new String(template, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return writer.toString().getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public byte[] render(Template template, Map<String, Object> objects, Type type) {
        return render(template.getTemplate(), objects, type);
    }

    @Inject
    public void setProperties(@VelocityProperties Properties properties) {
        this.properties = properties;
    }

}
