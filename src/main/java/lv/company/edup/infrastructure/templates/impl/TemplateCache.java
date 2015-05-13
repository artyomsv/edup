package lv.company.edup.infrastructure.templates.impl;

import lv.company.edup.infrastructure.templates.api.Template;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TemplateCache {

    private Map<TemplateName, Template> templates;

    private Logger logger = Logger.getLogger(TemplateCache.class.getSimpleName());

    @PostConstruct
    public void init() {
        templates = new EnumMap<>(TemplateName.class);
        try {
            for (TemplateName name : TemplateName.values()) {
                InputStream stream = TemplateCache.class.getResourceAsStream(name.file);
                String template = IOUtils.toString(stream, "UTF-8");
                templates.put(name, new Template(name, template));
                logger.log(Level.INFO, "File {0} was caches as velocity template", name.file);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Template getTemplate(TemplateName name) {
        return templates.get(name);
    }

}
