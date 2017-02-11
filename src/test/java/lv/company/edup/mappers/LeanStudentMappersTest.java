package lv.company.edup.mappers;

import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.properties.PropertyName;
import lv.company.edup.persistence.students.version.StudentVersion;
import lv.company.edup.services.students.dto.LeanStudentDto;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LeanStudentMappersTest extends AbstractMappersTest {

    public static final String PERSON_ID = "201281-15151";
    public static final String NAME = "Artyom";
    public static final String LAST_NAME = "Stukans";
    public static final long ID = 1L;
    public static final long VERSION_ID = 2L;
    public static final long AGE = 5L;
    public static final String MOBILE = "28612816";
    public static final Date BIRTH_DATE = DateUtils.addYears(new Date(), (int) (AGE) * -1);

    @Test
    public void entityToBaseDto() throws Exception {
        CurrentStudentVersion entity = new CurrentStudentVersion();
        entity.setId(ID);
        entity.setVersionId(VERSION_ID);
        entity.setCreated(new Date());
        entity.setName(NAME);
        entity.setLastName(LAST_NAME);
        entity.addProperty(PropertyName.BIRTH_DATE, BIRTH_DATE);
        entity.addProperty(PropertyName.MOBILE_PHONE, MOBILE);
        entity.addProperty(PropertyName.PERSONAL_NUMBER, PERSON_ID);

        LeanStudentDto dto = mapper.map(entity, LeanStudentDto.class);

        assertNotNull(dto);
        assertThat(dto.getId(), is(ID));
        assertThat(dto.getVersionId(), is(VERSION_ID));
        assertThat(dto.getName(), is(NAME));
        assertThat(dto.getLastName(), is(LAST_NAME));
        assertThat(dto.getAge(), is(AGE));
        assertThat(dto.getPersonId(), is(PERSON_ID));
        assertThat(dto.getMobile(), is(MOBILE));
        assertTrue(org.apache.commons.lang.time.DateUtils.isSameDay(dto.getBirthDate(), BIRTH_DATE));

    }

    @Test
    public void baseDtoToEntityVersion() throws Exception {
        LeanStudentDto dto = new LeanStudentDto();
        dto.setId(ID);
        dto.setVersionId(VERSION_ID);
        dto.setLastName(LAST_NAME);
        dto.setName(NAME);
        dto.setMobile(MOBILE);
        dto.setPersonId(PERSON_ID);
        dto.setAge(AGE);
        dto.setBirthDate(BIRTH_DATE);

        StudentVersion entity = mapper.map(dto, StudentVersion.class);

        assertNotNull(entity);
        assertThat(entity.getId(), is(ID));
        assertThat(entity.getVersionId(), is(VERSION_ID));
        assertThat(entity.getName(), is(NAME));
        assertThat(entity.getLastName(), is(LAST_NAME));

        assertThat(entity.getProperties().size(), is(3));
        assertTrue(DateUtils.isSameDay(entity.getDateProperty(PropertyName.BIRTH_DATE), BIRTH_DATE));
        assertThat(entity.getStringProperty(PropertyName.PERSONAL_NUMBER), is(PERSON_ID));
        assertThat(entity.getStringProperty(PropertyName.MOBILE_PHONE), is(MOBILE));
    }
}
