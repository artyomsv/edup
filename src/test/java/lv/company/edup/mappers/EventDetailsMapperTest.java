package lv.company.edup.mappers;

import lv.company.edup.persistence.subjects.EventStatus;
import lv.company.edup.persistence.subjects.view.SubjectEventsDetails;
import lv.company.edup.services.subjects.dto.EventDetailsDto;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class EventDetailsMapperTest extends AbstractMappersTest {

    public static final Date EVENT_DATE = new Date();
    public static final Date FROM = DateUtils.addHours(EVENT_DATE, 10);
    public static final Date TO = DateUtils.addHours(EVENT_DATE, 11);

    public static final long EVENT_ID = 2L;
    public static final String SUBJECT_NAME = "Subject name";
    public static final EventStatus STATUS = EventStatus.PLANNED;
    public static final long PRICE = 1200L;

    @Test
    public void testName() throws Exception {
        SubjectEventsDetails entity = new SubjectEventsDetails();
        entity.setName(SUBJECT_NAME);
        entity.setStatus(STATUS);
        entity.setEventDate(EVENT_DATE);
        entity.setFrom(FROM);
        entity.setTo(TO);
        entity.setEventId(EVENT_ID);
        entity.setPrice(PRICE);

        EventDetailsDto dto = mapper.map(entity, EventDetailsDto.class);

        assertNotNull(dto);
        assertNotNull(dto.getSubject());
        assertThat(dto.getSubject().getSubjectName(), is(SUBJECT_NAME));
        assertThat(dto.getEventId(), is(EVENT_ID));
        assertThat(dto.getStatus(), is(STATUS));
        assertThat(dto.getEventDate(), is(EVENT_DATE));
        assertThat(dto.getFrom(), is(FROM));
        assertThat(dto.getTo(), is(TO));
        assertThat(dto.getPrice(), is(PRICE));

    }
}
