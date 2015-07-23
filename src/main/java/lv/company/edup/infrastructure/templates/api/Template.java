package lv.company.edup.infrastructure.templates.api;

public class Template {

    private final TemplateName name;
    private final byte[] template;
    private final byte[] compiledTemplate;

    public Template(byte[] compiledTemplate, byte[] template, TemplateName name) {
        this.compiledTemplate = compiledTemplate;
        this.template = template;
        this.name = name;
    }

    public TemplateName getName() {
        return name;
    }

    public byte[] getTemplate() {
        return template;
    }

    public byte[] getCompiledTemplate() {
        return compiledTemplate;
    }
}
