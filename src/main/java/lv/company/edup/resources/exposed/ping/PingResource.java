package lv.company.edup.resources.exposed.ping;

import lv.company.edup.infrastructure.lucene.api.config.IndexType;
import lv.company.edup.infrastructure.lucene.api.config.StudentWriter;
import lv.company.edup.infrastructure.lucene.impl.indexer.StudentsIndexer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("public/ping")
@ApplicationScoped
public class PingResource {

    @Inject PingFacade facade;
    @Inject @StudentWriter StudentsIndexer indexer;

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response pong() {
        return facade.buildDto();
    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public Response pongHtml() {
        IndexType type = indexer.getType();
        return facade.buildHtml();
    }


}
