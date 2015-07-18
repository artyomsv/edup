package lv.company.edup.services.subjects.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.persistence.subjects.view.EventView;
import lv.company.edup.services.subjects.dto.EventDto;
import ma.glasnost.orika.MapperFactory;

public class EventMapper implements CustomMapper {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(EventView.class, EventDto.class)
                .field("subjectId", "subject.subjectId")
                .field("name", "subject.subjectName")
                .byDefault()
                .register();
    }

}
