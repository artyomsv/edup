package lv.company.edup.services.subjects.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.persistence.subjects.view.SubjectEvents;
import lv.company.edup.services.subjects.dto.EventDto;
import ma.glasnost.orika.MapperFactory;

public class EventMapper implements CustomMapper {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(SubjectEvents.class, EventDto.class)
                .field("name", "subject.subjectName")
                .byDefault()
                .register();
    }

}
