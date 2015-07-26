package lv.company.edup.services.subjects;

import lv.company.edup.persistence.subjects.view.SubjectEvents;
import lv.company.edup.persistence.subjects.view.SubjectEvents_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class EventODataMapping extends AbstractODataJPAMapping<SubjectEvents> {

    public enum Mapping {
        EventId, SubjectId, Status, EventDate, From, To
    }

    public EventODataMapping() {
        mapAttribute(Mapping.EventDate).to(SubjectEvents_.eventId);
        mapAttribute(Mapping.SubjectId).to(SubjectEvents_.subjectId);
        mapAttribute(Mapping.Status).to(SubjectEvents_.status);
        mapAttribute(Mapping.EventDate).to(SubjectEvents_.eventDate);
        mapAttribute(Mapping.From).to(SubjectEvents_.from);
        mapAttribute(Mapping.To).to(SubjectEvents_.to);
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
