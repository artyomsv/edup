package lv.company.edup.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.odata.api.ODataResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ApplicationFacade {

    @Inject UriUtils utils;

    public Response ok() {
        return Response.ok().build();
    }

    public Response ok(String o) {
        if (o != null) {
            return prepare(Response.Status.OK, o);
        } else {
            return notFound();
        }
    }

    public Response ok(Object o) {
        if (o != null) {
            return prepare(Response.Status.OK, o);
        } else {
            return notFound();
        }
    }

    public Response ok(ODataResult o) {
        if (o != null) {
            return prepare(Response.Status.OK, o);
        } else {
            return notFound();
        }
    }

    public Response ok(Collection c) {
        return ok(new ArrayList(c));
    }

    public Response ok(List list) {
        if (CollectionUtils.isEmpty(list)) {
            return notFound();
        } else {
            ODataResult response = new ODataResult();
            response.setValues(list);
            return prepare(Response.Status.OK, response);
        }
    }

    public Response created(Long id, Long versionId) {
        String entity = String.format("{\"payload\": %s}", id);
        return Response
                .created(utils.buildCreated(id))
                .header(HttpHeaders.ETAG, String.valueOf(versionId))
                .entity(entity)
                .build();
    }

    public Response created(Long id) {
        String entity = String.format("{\"payload\": %s}", id);
        return Response
                .created(utils.buildCreated(id))
                .entity(entity)
                .build();
    }

    public Response updated(Long versionId) {
        String entity = String.format("{\"payload\": %s}", versionId);
        return Response.ok()
                .header(HttpHeaders.ETAG, String.valueOf(versionId))
                .entity(entity)
                .build();
    }

    public Response notFound() {
        return Response.noContent().build();
    }

    public Response streamResponse(final byte[] data, String contentType, String fileName) {
        return Response.status(Response.Status.OK)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .expires(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)))
                .header(HttpHeaders.CACHE_CONTROL, "maxAge=" + TimeUnit.DAYS.toSeconds(7) + ", private")
                .header("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName))
                .entity(new StreamingOutput() {
                            @Override
                            public void write(OutputStream output) throws IOException, WebApplicationException {
                                try {
                                    IOUtils.write(data, output);
                                } finally {
                                    IOUtils.closeQuietly(output);
                                }
                            }
                        }

                ).build();
    }

    private Response prepare(Response.Status status) {
        return Response.status(status).build();
    }

    private <T> Response prepare(Response.Status status, String response) {
        return Response.status(status).entity(response).build();
    }

    private <T> Response prepare(Response.Status status, T o) {
        try {
            String entity = String.format("{\"payload\": %s}", new ObjectMapper().writeValueAsString(o));
            return Response.status(status).entity(entity).build();
        } catch (JsonProcessingException e) {
            throw new InternalException(e);
        }
    }

    private Response prepare(Response.Status status, ODataResult o) {
        return Response.status(status).entity(o).build();
    }
}
