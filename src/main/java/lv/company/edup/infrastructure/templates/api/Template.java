package lv.company.edup.infrastructure.templates.api;

public class Template {

    private TemplateName name;
    private String template;

    public Template() {
    }

    public Template(TemplateName name, String template) {
        this.name = name;
        this.template = template;
    }

    public TemplateName getName() {
        return name;
    }

    public void setName(TemplateName name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
