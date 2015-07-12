package lv.company.edup.services.subjects;

import lv.company.edup.persistence.subjects.view.EventView;
import lv.company.edup.persistence.subjects.view.EventView_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class EventODataMapping extends AbstractODataJPAMapping<EventView> {

    public EventODataMapping() {
        mapAttribute("EventId").to(EventView_.eventId);
        mapAttribute("SubjectId").to(EventView_.subjectId);
        mapAttribute("Status").to(EventView_.status);
        mapAttribute("EventDate").to(EventView_.eventDate);
        mapAttribute("Created").to(EventView_.created);
        mapAttribute("Updated").to(EventView_.updated);
    }

    @Override
    public Class<EventView> getEntityType() {
        return EventView.class;
    }

    @Override
    public SingularAttribute<EventView, ?> getPrimaryKeyAttribute() {
        return EventView_.eventId;
    }

}
