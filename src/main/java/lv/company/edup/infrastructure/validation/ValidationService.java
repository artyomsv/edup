package lv.company.edup.infrastructure.validation;

import lv.company.edup.infrastructure.exceptions.EdupConstraintViolationException;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.Set;

@Singleton
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Startup
public class ValidationService {

    private Validator validator;

    public void validate(Object... objects) {
        validate(new Class<?>[]{Default.class}, objects);
    }

    public void validate(Class<?> defaultGroup, Object... objects) {
        validate(new Class<?>[]{defaultGroup}, objects);
    }

    public void validate(Class<?>[] groups, Object... objects) {
        if (groups == null) {
            groups = new Class<?>[]{Default.class};
        }

        Set<ConstraintViolation<?>> result = null;
        for (Object o : objects) {
            Set<ConstraintViolation<Object>> violations = validator.validate(o, groups);
            if (CollectionUtils.isNotEmpty(violations)) {
                result = initResult(result);
                CollectionUtils.addAll(result, violations);
            }
        }

        if (CollectionUtils.isNotEmpty(result)) {
            throw new EdupConstraintViolationException(result);
        }
    }

    private Set<ConstraintViolation<?>> initResult(Set<ConstraintViolation<?>> result) {
        if (result == null) {
            result = new HashSet<>();
        }
        return result;
    }

    @Inject
    public void setValidator(Validator validator) {
        this.validator = validator;
    }
}
