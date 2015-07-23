package lv.company.edup.infrastructure.templates.api;

public enum TemplateName {

    FakturaJasper("/templates/faktura_rekins.jrxml", "/templates/faktura_rekins.jasper");

    public String file;
    public String compiledFile;

    TemplateName(String file, String compiledFile) {
        this.file = file;
        this.compiledFile = compiledFile;
    }
}
