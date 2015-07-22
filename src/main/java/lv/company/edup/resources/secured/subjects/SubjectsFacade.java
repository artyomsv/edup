package lv.company.edup.resources.secured.subjects;

import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.validation.Validated;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.subjects.SubjectsService;
import lv.company.edup.services.subjects.dto.AttendanceDto;
import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;
import lv.company.odata.api.ODataCriteria;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class SubjectsFacade extends ApplicationFacade {

    @Inject SubjectsService subjectsService;
    @Inject UriUtils utils;

    public Response searchSubjects() {
        ODataCriteria criteria = new ODataCriteria(utils.getQueryParameters());

        if (!criteria.isNotEmpty()) {
            criteria.setSearch("*");
        }

        return ok(subjectsService.search(criteria));
    }

    @Validated
    public Response createSubject(@Valid SubjectDto dto) {
        return created(subjectsService.createSubject(dto));
    }

    public Response searchSubjectEvents() {
        ODataCriteria criteria = new ODataCriteria(utils.getQueryParameters());
        return ok(subjectsService.searchSubjectEvents(criteria));
    }

    @Validated
    public Response createSubjectEvent(@Valid EventDto dto) {
        return created(subjectsService.createSubjectEvent(dto));
    }

    public Response searchAttendance() {
        ODataCriteria criteria = new ODataCriteria(utils.getQueryParameters());
        return ok(subjectsService.searchAttendance(criteria));
    }

    @Validated
    public Response saveEventAttendance(@Valid AttendanceDto dto, Long eventId) {
        return created(subjectsService.createAttendance(dto, eventId));
    }

    @Validated
    public Response updateEventAttendance(@Valid AttendanceDto dto, Long eventId, Long attendanceId) {
        subjectsService.updateAttendance(dto, eventId, attendanceId);
        return ok();
    }

    public Response deleteEventAttendance(Long eventId, Long attendanceId) {
        subjectsService.deleteAttendance(eventId, attendanceId);
        return ok();
    }

    public Response rebuildSubjects() {
        subjectsService.rebuildIndex();
        return ok();
    }

    public Response fillSubjectEvents(long subjectId) {
        subjectsService.generateSubjectEvents(subjectId);
        return ok();
    }

    public Response getSubjectEventDetails(Long eventId) {
        return ok(subjectsService.getEventDetails(eventId));
    }
}
