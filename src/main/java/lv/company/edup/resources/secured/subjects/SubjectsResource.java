package lv.company.edup.resources.secured.subjects;

import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("private/subjects")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
public class SubjectsResource {

    @Inject SubjectsFacade facade;

    @GET
    public Response searchSubjects() {
        return facade.searchSubjects();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSubject(SubjectDto dto) {
        return facade.createSubject(dto);
    }

    @GET
    @Path("events")
    public Response getSubjectEvents() {
        return facade.searchSubjectEvents();
    }

    @POST
    @Path("{subjectId}/events")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSubjectEvent(EventDto dto, @PathParam("subjectId") Long subjectId) {
        return facade.createSubjectEvent(dto, subjectId);
    }

    @GET
    @Path("{subjectId}/events/{eventId}/attendance")
    public Response getEventAttendance(@PathParam("subjectId") Long subjectId,
                                       @PathParam("eventId") Long eventId) {
        return facade.getEventAttendance(subjectId, eventId);
    }

}
