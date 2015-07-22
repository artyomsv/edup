package lv.company.edup.services.subjects;

import lv.company.edup.persistence.subjects.view.SubjectEvents;
import lv.company.edup.persistence.subjects.view.SubjectEvents_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class EventODataMapping extends AbstractODataJPAMapping<SubjectEvents> {

    public EventODataMapping() {
        mapAttribute("EventId").to(SubjectEvents_.eventId);
        mapAttribute("Status").to(SubjectEvents_.status);
        mapAttribute("EventDate").to(SubjectEvents_.eventDate);
        mapAttribute("From").to(SubjectEvents_.from);
        mapAttribute("To").to(SubjectEvents_.to);
    }

    @Override
    public Class<SubjectEvents> getEntityType() {
        return SubjectEvents.class;
    }

    @Override
    public SingularAttribute<SubjectEvents, ?> getPrimaryKeyAttribute() {
        return SubjectEvents_.eventId;
    }

}
