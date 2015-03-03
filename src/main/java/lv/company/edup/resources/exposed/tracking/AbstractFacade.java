package lv.company.edup.resources.exposed.tracking;

import org.apache.commons.collections4.CollectionUtils;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

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
}
