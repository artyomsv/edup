package lv.company.edup.resources.exposed.ping;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("public/ping")
@ApplicationScoped
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class PingResource {

    @Inject PingFacade facade;

    @GET
    public Response pong() {
        return facade.buildDto();
    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public Response pongHtml() {
        return facade.buildHtml();
    }

}
