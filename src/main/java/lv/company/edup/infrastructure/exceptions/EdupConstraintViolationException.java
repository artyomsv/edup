package lv.company.edup.infrastructure.exceptions;

import javax.ejb.ApplicationException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ApplicationException(rollback = true)
public class EdupConstraintViolationException extends ConstraintViolationException {

    public EdupConstraintViolationException(String message, Set<ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }

    public EdupConstraintViolationException(Set<ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

}
