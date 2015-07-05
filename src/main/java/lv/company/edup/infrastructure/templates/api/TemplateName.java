package lv.company.edup.infrastructure.templates.api;

public enum TemplateName {

    FakturaJasper("/templates/faktura.xml");

    public String file;

    TemplateName(String file) {
        this.file = file;
    }
}
