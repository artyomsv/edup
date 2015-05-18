package lv.company.edup.resources.exposed.files;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path("files")
@ApplicationScoped
public class FilesResource {

    @Inject FileFacade facade;

    @GET
    @Path("{id}")
    @Produces(APPLICATION_JSON)
    public Response getFileInformation(@PathParam("id") Long id) {
        return facade.getFileInformation(id);
    }

    @GET
    @Path("download/{id}")
    @Produces({TEXT_HTML, APPLICATION_JSON})
    public Response downloadFile(@PathParam("id") Long id) {
        return facade.downloadFile(id);
    }

    @POST
    @Path("upload")
    @Consumes(MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    public Response upload(@Context HttpServletRequest request) {
        return facade.uploadFile(request);
    }


}
