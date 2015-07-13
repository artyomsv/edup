package lv.company.edup.infrastructure.validation;

import lv.company.edup.infrastructure.exceptions.EdupConstraintViolationException;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class ValidationService {

    private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator;

    @PostConstruct
    public void init() {
        validator = factory.getValidator();
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

}
