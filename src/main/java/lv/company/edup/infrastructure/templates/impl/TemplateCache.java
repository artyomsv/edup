package lv.company.edup.infrastructure.templates.impl;

import lv.company.edup.infrastructure.templates.api.Template;
import lv.company.edup.infrastructure.templates.api.TemplateName;
import org.apache.commons.io.IOUtils;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
                templates.put(name, new Template(read(name.compiledFile), read(name.file), name));
                logger.log(Level.INFO, "File {0} was caches as template", name.file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] read(String file) throws IOException {
        InputStream stream = null;
        try {
            stream = TemplateCache.class.getResourceAsStream(file);
            return IOUtils.toByteArray(stream);
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public Template getTemplate(TemplateName name) {
        return templates.get(name);
    }

}
