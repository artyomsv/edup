package lv.company.edup.persistence.subjects;

import lv.company.edup.persistence.EdupRepository;
import lv.company.edup.persistence.subjects.domain.Event;

import javax.ejb.Stateless;

@Stateless
public class EventRepository extends EdupRepository<Event> {

    @Override
    public Class<Event> getEntityClass() {
        return Event.class;
    }

}
