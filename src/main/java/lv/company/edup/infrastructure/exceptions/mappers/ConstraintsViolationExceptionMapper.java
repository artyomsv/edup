package lv.company.edup.infrastructure.exceptions.mappers;

import lv.company.edup.infrastructure.exceptions.EdupConstraintViolationException;
import lv.company.edup.infrastructure.exceptions.dto.ErrorData;
import lv.company.edup.infrastructure.exceptions.dto.ErrorDto;
import lv.company.edup.infrastructure.response.ErrorCode;
import lv.company.edup.infrastructure.response.ErrorResponseProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collection;
import java.util.Set;

@Provider
@ApplicationScoped
public class ConstraintsViolationExceptionMapper implements ExceptionMapper<EdupConstraintViolationException> {

    @Inject ErrorResponseProvider provider;

    @Override
    public Response toResponse(EdupConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        Collection<ErrorDto> dtos = CollectionUtils.collect(violations, new Transformer<ConstraintViolation<?>, ErrorDto>() {
            @Override
            public ErrorDto transform(ConstraintViolation<?> input) {
                ErrorDto dto = new ErrorDto();
                dto.setMessage(input.getMessage());
                dto.setCode(ErrorCode.CONSTRAINT_FAILED);
                Path path = input.getPropertyPath();
                if (path != null) {
                    dto.setPath(String.valueOf(path));
                }

                return dto;
            }
        });

        ErrorData data = new ErrorData();
        data.setErrors(dtos);

        return provider.badRequest(data);
    }

}
