package lv.company.edup.infrastructure.response;

import lv.company.edup.infrastructure.exceptions.dto.ErrorData;
import lv.company.edup.infrastructure.exceptions.dto.ErrorDto;
import org.apache.commons.collections4.CollectionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Collection;

@ApplicationScoped
public class ResponseProvider {

    @Inject UriUtils utils;


    public Response ok() {
        return prepare(Response.Status.OK);
    }

    public Response ok(Object o) {
        if (o != null) {
            return prepare(Response.Status.OK, o);
        } else {
            return notFound();
        }
    }

    public Response ok(Collection c) {
        if (CollectionUtils.isEmpty(c)) {
            return notFound();
        } else {
            CommonResponse response = new CommonResponse();
            response.setPayload(c);
            return prepare(Response.Status.OK, response);
        }
    }

    public Response notFound() {
        return prepare(Response.Status.NOT_FOUND);
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

    private Response prepare(Response.Status status) {
        return Response.status(status).build();
    }

    private Response prepare(Response.Status status, Object o) {
        return Response.status(status).entity(o).build();
    }
}
