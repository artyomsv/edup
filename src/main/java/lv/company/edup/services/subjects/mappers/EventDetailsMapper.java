package lv.company.edup.services.subjects.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.persistence.subjects.view.SubjectEventsDetails;
import lv.company.edup.services.subjects.dto.EventDetailsDto;
import ma.glasnost.orika.MapperFactory;

public class EventDetailsMapper implements CustomMapper {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(SubjectEventsDetails.class, EventDetailsDto.class)
                .field("name", "subject.subjectName")
                .byDefault()
                .register();
    }

}
