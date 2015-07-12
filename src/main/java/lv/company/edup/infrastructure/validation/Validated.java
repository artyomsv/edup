package lv.company.edup.infrastructure.validation;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import javax.validation.groups.Default;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validated {

    @Nonbinding Class<?>[] groups() default Default.class;

}
