package lv.company.edup.resources.exposed.ping;

import lv.company.edup.infrastructure.lucene.api.indexer.StudentWriter;
import lv.company.edup.infrastructure.lucene.api.indexer.SubjectWriter;
import lv.company.edup.infrastructure.lucene.impl.indexer.StudentsIndexWriter;
import lv.company.edup.infrastructure.lucene.impl.indexer.SubjectsIndexWriter;

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
    @Inject @StudentWriter StudentsIndexWriter studentsIndex;
    @Inject @SubjectWriter SubjectsIndexWriter subjectIndex;

    @GET
    public Response pong() {
        return facade.buildDto();
    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public Response pongHtml() {
        return facade.buildHtml();
    }

    @GET
    @Path("index")
    public Response rebuildIndex() {
        studentsIndex.fullRebuild();
        subjectIndex.fullRebuild();
        return facade.ok();
    }



}
