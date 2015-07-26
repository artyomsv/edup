package lv.company.edup.infrastructure.templates.api;

import java.util.Map;

public interface TemplateEngine {

    byte[] render(byte[] template, Map<String, Object> context, Type type);

    byte[] render(Template template, Map<String, Object> context, Type type);

    void init();

}
