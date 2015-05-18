package lv.company.edup.infrastructure.exceptions.mappers;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.response.ErrorCode;
import lv.company.edup.infrastructure.response.ResponseProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class InternalErrorExceptionMapper implements ExceptionMapper<InternalException> {

    @Inject ResponseProvider provider;

    @Override
    public Response toResponse(InternalException exception) {
        Throwable cause = exception.getCause();
        return provider.error(cause, ErrorCode.INTERNAL_ERROR);
    }

}
