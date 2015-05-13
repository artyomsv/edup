package lv.company.edup.infrastructure.templates.api;

public enum TemplateName {

    VisitorJournal("/templates/visiting_journal.vm");

    public String file;

    TemplateName(String file) {
        this.file = file;
    }
}
