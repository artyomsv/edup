package lv.company.edup.validation;

import lv.company.edup.infrastructure.exceptions.EdupConstraintViolationException;
import lv.company.edup.infrastructure.validation.ValidationService;
import lv.company.edup.services.students.dto.StudentBalanceDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;

import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BalanceValidationTest {

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
    public void validateEmptyAmountBalance() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        dto.setStudentId(1L);
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertConstraint(e.getConstraintViolations().iterator().next(), "amount", "may not be null");
        }
    }

    @Test
    public void validateNegativeStudentBalance() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        dto.setStudentId(-1L);
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(2));
            assertConstraint(find(e.getConstraintViolations(), "studentId"), "studentId", "Student ID cannot be negative!");
            assertConstraint(find(e.getConstraintViolations(), "amount"), "amount", "may not be null");
        }
    }

    @Test
    public void validateEmptyStudentIdBalance() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        dto.setAmount(1L);
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertConstraint(e.getConstraintViolations().iterator().next(), "studentId", "may not be null");
        }
    }

    @Test
    public void validateNegativeAmountBalance() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        dto.setAmount(-1L);
        dto.setStudentId(1L);
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(1));
            assertConstraint(find(e.getConstraintViolations(), "amount"), "amount", "Amount have to be grater then zero!");
        }
    }

    @Test
    public void validateEmptyBalance() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(2));
            assertConstraint(find(e.getConstraintViolations(), "amount"), "amount", "may not be null");
            assertConstraint(find(e.getConstraintViolations(), "studentId"), "studentId", "may not be null");
        }
    }


    @Test
    public void validateValidComment() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        dto.setStudentId(1L);
        dto.setAmount(1L);
        String comment = "";
        for (int i = 0; i < 256; i++) {
            comment += "1";
        }
        dto.setComments(comment);
        assertThat(comment.length(), is(256));
        validationService.validate(dto);
    }

    @Test
    public void validateNotValidComment() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        dto.setStudentId(1L);
        dto.setAmount(1L);
        String comment = "";
        for (int i = 0; i < 257; i++) {
            comment += "1";
        }
        dto.setComments(comment);
        assertThat(comment.length(), is(257));
        try {
        validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(1));
            assertConstraint(find(e.getConstraintViolations(), "comments"), "comments", "Transaction comment must be between 0 and 256 characters long");
        }
    }

    @Test
    public void validateNotValidLargeAmountComment() throws Exception {
        StudentBalanceDto dto = new StudentBalanceDto();
        dto.setStudentId(1L);
        dto.setAmount(100001L);
        try {
            validationService.validate(dto);
        } catch (EdupConstraintViolationException e) {
            assertThat(e.getConstraintViolations().size(), is(1));
            assertConstraint(find(e.getConstraintViolations(), "amount"), "amount", "Maximum amount must not exceed 1000.00 EURO");
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
