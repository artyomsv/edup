package lv.company.edup.resources;

import lv.company.edup.infrastructure.response.CommonResponse;
import lv.company.edup.infrastructure.response.UriUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EdupFacade {

    @Inject UriUtils utils;

    public Response ok() {
        return Response.ok().build();
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

    public Response created(Long id) {
        return Response.created(utils.buildCreated(id)).build();
    }

    public Response notFound() {
        return Response.noContent().build();
    }

    public Response streamResponse(final byte[] data, String contentType) {
        return Response.status(Response.Status.OK)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .expires(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)))
                .header(HttpHeaders.CACHE_CONTROL, "maxAge=" + TimeUnit.DAYS.toSeconds(7) + ", private")
                .header("Content-Disposition", String.format("attachment; filename=\"%s\"", "visiting_journal.pdf"))
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

    private Response prepare(Response.Status status, Object o) {
        return Response.status(status).entity(o).build();
    }
}
