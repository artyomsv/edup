package lv.company.edup.infrastructure.templates.api;

public enum TemplateName {

    VisitorJournal("/templates/visiting_journal.vm"),
    Faktura("/templates/faktura.vm");

    public String file;

    TemplateName(String file) {
        this.file = file;
    }
}
