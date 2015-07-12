package lv.company.edup.infrastructure.validation;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.groups.Default;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Validated
@Interceptor
public class ValidationInterceptor {

    @Inject ValidationService validationService;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        Object[] parameters = context.getParameters();
        Method method = context.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        Set<Object> forValidation = new HashSet<>(parameters.length);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            boolean found = false;
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType().isAssignableFrom(Validate.class)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                if (parameters[i] instanceof Collection) {
                    forValidation.addAll((Collection<?>) parameters[i]);
                } else {
                    forValidation.add(parameters[i]);
                }
            }
        }

        Validated annotation = method.getAnnotation(Validated.class);
        Object[] objects = forValidation.toArray(new Object[forValidation.size()]);

        if (annotation != null) {
            validationService.validate(annotation.groups(), objects);
        } else {
            validationService.validate(Default.class, objects);
        }

        return context.proceed();

    }

}
