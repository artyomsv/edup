package lv.company.edup.resources.secured.subjects;

import lv.company.edup.services.subjects.dto.AttendanceDto;
import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;

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
    @Path("events")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveSubjectEvent(EventDto dto) {
        return facade.createSubjectEvent(dto);
    }

    @GET
    @Path("events/attendance")
    public Response getEventAttendance() {
        return facade.getEventAttendance();
    }

    @POST
    @Path("events/{eventId}/attendance")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveEventAttendance(AttendanceDto dto,
                                        @PathParam("eventId") Long eventId) {
        return facade.saveEventAttendance(dto, eventId);
    }

    @PUT
    @Path("events/{eventId}/attendance/{attendanceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEventAttendance(AttendanceDto dto,
                                          @PathParam("eventId") Long eventId,
                                          @PathParam("attendanceId") Long attendanceId) {
        return facade.updateEventAttendance(dto, eventId, attendanceId);
    }

    @DELETE
    @Path("events/{eventId}/attendance/{attendanceId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateEventAttendance(@PathParam("eventId") Long eventId,
                                          @PathParam("attendanceId") Long attendanceId) {
        return facade.deleteEventAttendance(eventId, attendanceId);
    }

}
