package lv.company.edup.resources.secured.students;

import lv.company.edup.services.students.dto.StudentDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("private/students")
@Produces(MediaType.APPLICATION_JSON)
public class StudentsResource {

    @Inject StudentsFacade facade;

    @GET
    public Response search() {
        return facade.search();
    }

    @GET
    @Path("rebuild")
    public Response rebuild() {
        return facade.rebuild();
    }

    @GET
    @Path("fill/{records}")
    public Response fill(@PathParam("records") Integer records) {
        return facade.fill(records);
    }

    @GET
    @Path("{id}")
    public Response findStudent(@PathParam("id") Long id) {
        return facade.findStudent(id);
    }

    @GET
    @Path("{id}/versions")
    public Response findVersions(@PathParam("id") Long id) {
        return facade.findVersions(id);
    }

    @GET
    @Path("{id}/versions/{versionId}")
    public Response findStudentVersion(@PathParam("id") Long id,
                                       @PathParam("versionId") Long versionId) {
        return facade.findVersion(id, versionId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createStudent(StudentDto dto) {
        return facade.createStudent(dto);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateStudent(StudentDto dto, @PathParam("id") Long id) {
        return facade.updateStudent(dto, id);
    }

    @DELETE
    @Path("{id}")
    public Response deleteStudent(@PathParam("id") Long id) {
        return facade.deleteStudent(id);
    }

    @GET
    @Path("{id}/balance")
    public Response getBalance(@PathParam("id") Long id) {
        return facade.getStudentBalance(id);
    }


}
