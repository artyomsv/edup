package lv.company.edup.mappers;

import lv.company.edup.persistence.subjects.EventStatus;
import lv.company.edup.persistence.subjects.view.SubjectEvents;
import lv.company.edup.services.subjects.dto.EventDto;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class EventMapperTest extends AbstractMappersTest {

    public static final Date EVENT_DATE = new Date();
    public static final Date FROM = DateUtils.addHours(EVENT_DATE, 10);
    public static final Date TO = DateUtils.addHours(EVENT_DATE, 11);

    public static final long EVENT_ID = 2L;
    public static final String SUBJECT_NAME = "Subject name";
    public static final EventStatus STATUS = EventStatus.PLANNED;

    @Test
    public void testName() throws Exception {
        SubjectEvents entity = new SubjectEvents();
        entity.setName(SUBJECT_NAME);
        entity.setStatus(STATUS);
        entity.setEventDate(EVENT_DATE);
        entity.setFrom(FROM);
        entity.setTo(TO);
        entity.setEventId(EVENT_ID);

        EventDto dto = mapper.map(entity, EventDto.class);

        assertNotNull(dto);
        assertNotNull(dto.getSubject());
        assertThat(dto.getSubject().getSubjectName(), is(SUBJECT_NAME));
        assertThat(dto.getEventId(), is(EVENT_ID));
        assertThat(dto.getStatus(), is(STATUS));
        assertThat(dto.getEventDate(), is(EVENT_DATE));
        assertThat(dto.getFrom(), is(FROM));
        assertThat(dto.getTo(), is(TO));

    }
}
