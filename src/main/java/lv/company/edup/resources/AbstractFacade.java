package lv.company.edup.resources;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class AbstractFacade {

    @Context private UriInfo uriInfo;

    public Response ok(Collection coll) {
        return CollectionUtils.isEmpty(coll) ? notFound() : Response.ok(coll).build();
    }

    public Response ok(Object o) {
        return o == null ? notFound() : Response.ok(o).build();
    }

    public Response created(Long id) {
        URI created = uriInfo.getAbsolutePathBuilder().build(id);
        return Response.created(created).build();
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

                ).

                        build();
    }
}
