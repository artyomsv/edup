package lv.company.edup.infrastructure.exceptions.mappers;

import lv.company.edup.infrastructure.exceptions.dto.ErrorData;
import lv.company.edup.infrastructure.exceptions.dto.ErrorDto;
import lv.company.edup.infrastructure.response.ErrorCode;
import lv.company.edup.infrastructure.response.ErrorResponseProvider;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class EdupConstraintsViolationExceptionMapper implements ExceptionMapper<ResteasyViolationException> {

    @Inject ErrorResponseProvider provider;

    @Override
    public Response toResponse(ResteasyViolationException exception) {

        ErrorData data = new ErrorData();
        for (ResteasyConstraintViolation violation : exception.getPropertyViolations()) {
            ErrorDto dto = new ErrorDto();
            dto.setCode(ErrorCode.CONSTRAINT_FAILED);
            dto.setPath(violation.getPath());
            dto.setMessage(violation.getMessage());
            dto.setType(String.valueOf(violation.getConstraintType()));
            dto.setValue(violation.getValue());
            data.getErrors().add(dto);
        }

        return provider.badRequest(data);
    }

}
