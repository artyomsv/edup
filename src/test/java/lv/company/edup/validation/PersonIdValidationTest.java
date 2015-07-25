package lv.company.edup.validation;

import lv.company.edup.infrastructure.exceptions.EdupConstraintViolationException;
import lv.company.edup.infrastructure.validation.ValidationService;
import lv.company.edup.services.students.dto.StudentDto;
import lv.company.edup.services.students.validation.StudentUpdateCheck;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PersonIdValidationTest {

    private ValidationService validationService;

    @Before
    public void setUp() throws Exception {
        validationService = new ValidationService();
        validationService.init();
    }

    @After
    public void tearDown() throws Exception {
        validationService = null;
    }

    @Test
    public void emptyStudent() throws Exception {
        StudentDto dto = new StudentDto();
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(2));
            Map<String, Collection<ConstraintViolation<?>>> map = e.get();
            assertConstraint(map.get("name").iterator().next(), "name", "Name is missing");
            assertConstraint(map.get("lastName").iterator().next(), "lastName", "Last name missing");
        }
    }

    @Test
    public void emptyStudentForUpdate() throws Exception {
        StudentDto dto = new StudentDto();
        try {
            validationService.validate(StudentUpdateCheck.class, dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(1));
            Map<String, Collection<ConstraintViolation<?>>> map = e.get();
            assertConstraint(map.get("versionId").iterator().next(), "versionId", "Student version may not be null");
        }
    }

    @Test
    public void wrongMail() throws Exception {
        StudentDto dto = new StudentDto();
        dto.setName("Name");
        dto.setLastName("LastName");
        dto.setMail("1");
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(1));
            Map<String, Collection<ConstraintViolation<?>>> map = e.get();
            assertConstraint(map.get("mail").iterator().next(), "mail", "not a well-formed email address");
        }
    }

    @Test
    public void correctPersonId() throws Exception {
        StudentDto dto = new StudentDto();
        dto.setName("Name");
        dto.setLastName("LastName");
        dto.setPersonId("281281-10562");
        validationService.validate(dto);
    }

    @Test
    public void wrongPersonId() throws Exception {
        StudentDto dto = new StudentDto();
        dto.setName("Name");
        dto.setLastName("LastName");
        dto.setPersonId("281281 10562");
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            System.out.println(e.getConstraintViolations());
            assertThat(e.getConstraintViolations().size(), is(1));
            Map<String, Collection<ConstraintViolation<?>>> map = e.get();
            assertConstraint(map.get("personId").iterator().next(), "personId", "Person number does not match pattern");
        }
    }

    @Test
    public void wrongPersonId2() throws Exception {
        StudentDto dto = new StudentDto();
        dto.setName("Name");
        dto.setLastName("LastName");
        dto.setPersonId("123456789012");
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            System.out.println(e.getConstraintViolations());
            assertThat(e.getConstraintViolations().size(), is(1));
            Map<String, Collection<ConstraintViolation<?>>> map = e.get();
            assertConstraint(map.get("personId").iterator().next(), "personId", "Person number does not match pattern");
        }
    }
    @Test
    public void wrongPersonId3() throws Exception {
        StudentDto dto = new StudentDto();
        dto.setName("Name");
        dto.setLastName("LastName");
        dto.setPersonId("qwertyuiopqewqeqeqewq");
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(2));
            Map<String, Collection<ConstraintViolation<?>>> map = e.get();
            Iterator<ConstraintViolation<?>> iterator = map.get("personId").iterator();
            assertConstraint(iterator.next(), "personId", "Person number does not match pattern");
            assertConstraint(iterator.next(), "personId", "Person number have to 12 letters length");
        }
    }

    private void assertConstraint(ConstraintViolation<?> next, String path, String message) {
        assertThat(next.getPropertyPath().toString(), is(path));
        assertThat(next.getMessage(), is(message));
    }

    private ConstraintViolation<?> find(Collection<ConstraintViolation<?>> constraints, final String path) {
        return CollectionUtils.find(constraints, new Predicate<ConstraintViolation<?>>() {
            @Override
            public boolean evaluate(ConstraintViolation<?> object) {
                return StringUtils.equalsIgnoreCase(path, String.valueOf(object.getPropertyPath()));
            }
        });
    }

}
