package lv.company.edup.validation;

import lv.company.edup.infrastructure.exceptions.EdupConstraintViolationException;
import lv.company.edup.infrastructure.validation.ValidationService;
import lv.company.edup.services.students.dto.StudentDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

public class StudentNameValidation {

    private ValidationService validationService;

    @Before
    public void setUp() throws Exception {
        validationService = new ValidationService();
        validationService.setValidator(Validation.buildDefaultValidatorFactory().getValidator());
    }

    @After
    public void tearDown() throws Exception {
        validationService = null;
    }

    @Test
    public void testName() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setName("123456789012345678901234567890123");
        studentDto.setLastName("123456789012345678901234567890123");
        try {
            validationService.validate(studentDto);
        } catch (EdupConstraintViolationException e) {
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                System.out.println(violation);
            }
        }

    }
}
