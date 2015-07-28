package lv.company.edup.resources.secured.subjects;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.validation.Validated;
import lv.company.edup.persistence.subjects.EventStatus;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.students.TransactionService;
import lv.company.edup.services.students.validation.UpdateCheck;
import lv.company.edup.services.subjects.SubjectsService;
import lv.company.edup.services.subjects.dto.AttendanceDto;
import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import org.apache.commons.lang3.ObjectUtils;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.core.Response;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubjectsFacade extends ApplicationFacade {

    @Inject SubjectsService subjectsService;
    @Inject UriUtils utils;
    @Inject TransactionService transactionService;

    public Response searchSubjects() {
        ODataCriteria criteria = new ODataCriteria(utils.getQueryParameters());

        if (!criteria.isNotEmpty()) {
            criteria.setSearch("*");
        }

        return ok(subjectsService.search(criteria));
    }

    @Validated
    public Response createSubject(@Valid SubjectDto dto) {
        ODataResult<SubjectDto> search = subjectsService.searchSubject(dto);
        if (search.getCount() != 0) {
            throw new BadRequestException("Subject name already used");
        }

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

    @Validated(groups = {UpdateCheck.class})
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response updateSubjectEvent(Long eventId, @Valid EventDto dto) {
        if (ObjectUtils.notEqual(eventId, dto.getEventId())) {
            throw new BadRequestException("EventID does not match in DTO and request");
        }
        subjectsService.updateEvent(dto);
        if (dto.getStatus() == EventStatus.FINALIZED) {
            transactionService.updateStudentsBalance(dto);
        }

        return ok();
    }
}
