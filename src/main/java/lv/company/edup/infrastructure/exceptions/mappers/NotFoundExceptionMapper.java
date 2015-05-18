package lv.company.edup.infrastructure.exceptions.mappers;

import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.response.ErrorResponseProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Inject ErrorResponseProvider provider;

    @Override
    public Response toResponse(NotFoundException exception) {
        return provider.notFound();
    }

}
