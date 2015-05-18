package lv.company.edup.infrastructure.exceptions.mappers;

import lv.company.edup.infrastructure.exceptions.ExternalException;
import lv.company.edup.infrastructure.response.ErrorCode;
import lv.company.edup.infrastructure.response.ResponseProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class ExternalErrorExceptionMapper implements ExceptionMapper<ExternalException> {

    @Inject ResponseProvider provider;

    @Override
    public Response toResponse(ExternalException exception) {
        Throwable cause = exception.getCause();
        return provider.error(cause, ErrorCode.EXTERNAL_ERROR);
    }

}
