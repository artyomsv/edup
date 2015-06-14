package lv.company.edup.resources.secured.documents;

import lv.company.edup.services.documents.dto.StudentDocumentDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("private/documents")
@ApplicationScoped
@Produces({MediaType.APPLICATION_JSON})
public class DocumentsResource {

    @Inject DocumentsFacade facade;

    @GET
    public Response search() {
        return facade.search();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createDocument(StudentDocumentDto dto) {
        return facade.createStudentDocument(dto);
    }
}
