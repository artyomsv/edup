package lv.company.edup.infrastructure.templates.api;

import java.util.Map;

public interface TemplateEngine {

    String render(Template template, Map<String, Object> context);

}
