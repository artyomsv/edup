package lv.company.edup.infrastructure.templates.api;

public enum TemplateName {

    FakturaJasper("/templates/faktura_rekins.jrxml"),
    EventPlanningJournalVelocity("/templates/event_planning.vm");

    public String file;

    TemplateName(String file) {
        this.file = file;
    }
}
