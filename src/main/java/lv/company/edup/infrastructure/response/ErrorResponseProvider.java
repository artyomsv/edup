package lv.company.edup.infrastructure.response;

import lv.company.edup.infrastructure.exceptions.dto.ErrorData;
import lv.company.edup.infrastructure.exceptions.dto.ErrorDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class ErrorResponseProvider {

    @Inject UriUtils utils;

    public Response notFound() {
        return Response.noContent().build();
    }

    public Response error(Throwable e, ErrorCode code) {
        ErrorDto dto = new ErrorDto();
        dto.setCode(code);
        dto.setMessage(e.getLocalizedMessage());
        dto.setCause(e.getCause() != null ? e.getCause().getLocalizedMessage() : null);
        dto.setPath(utils.getResourceUrl());

        ErrorData data = new ErrorData();
        data.getErrors().add(dto);
        return prepare(Response.Status.INTERNAL_SERVER_ERROR, data);
    }

    public Response badRequest(String message) {
        ErrorDto dto = new ErrorDto();
        dto.setCode(ErrorCode.BAD_REQUEST);
        dto.setMessage(message);
        dto.setPath(utils.getResourceUrl());

        ErrorData data = new ErrorData();
        data.getErrors().add(dto);
        return prepare(Response.Status.BAD_REQUEST, data);
    }

    public Response badRequest(ErrorDto dto) {
        dto.setPath(utils.getResourceUrl());
        ErrorData data = new ErrorData();
        data.getErrors().add(dto);
        return prepare(Response.Status.BAD_REQUEST, data);
    }

    public Response badRequest(ErrorData data) {
        return prepare(Response.Status.BAD_REQUEST, data);
    }

    private Response prepare(Response.Status status, Object o) {
        return Response.status(status).entity(o).build();
    }
}
