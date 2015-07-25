package lv.company.edup.infrastructure.exceptions;

import lv.company.edup.infrastructure.utils.builder.IndexBuilder;
import org.apache.commons.collections4.Transformer;

import javax.ejb.ApplicationException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

@ApplicationException(rollback = true)
public class EdupConstraintViolationException extends ConstraintViolationException {

    public EdupConstraintViolationException(String message, Set<ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }

    public EdupConstraintViolationException(Set<ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

    public Map<String, Collection<ConstraintViolation<?>>> get() {
        return IndexBuilder.<String, ConstraintViolation<?>>get()
                .key(new Transformer<ConstraintViolation<?>, String>() {
                    @Override
                    public String transform(ConstraintViolation<?> input) {
                        return input.getPropertyPath() != null ? input.getPropertyPath().toString() : null;
                    }
                })
                .comporator(new Comparator<ConstraintViolation<?>>() {
                    @Override
                    public int compare(ConstraintViolation<?> o1, ConstraintViolation<?> o2) {
                        return o1.getMessage().compareTo(o2.getMessage());
                    }
                })
                .mapToCollection(getConstraintViolations());
    }

}
