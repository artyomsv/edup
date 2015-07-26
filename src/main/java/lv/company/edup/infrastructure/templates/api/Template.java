package lv.company.edup.infrastructure.templates.api;

public class Template {

    private final TemplateName name;
    private final byte[] template;

    public Template(byte[] template, TemplateName name) {
        this.template = template;
        this.name = name;
    }

    public TemplateName getName() {
        return name;
    }

    public byte[] getTemplate() {
        return template;
    }

}
