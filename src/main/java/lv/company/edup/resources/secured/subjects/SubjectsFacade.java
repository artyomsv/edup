package lv.company.edup.resources.secured.subjects;

import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.validation.Validate;
import lv.company.edup.infrastructure.validation.Validated;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.subjects.SubjectsService;
import lv.company.edup.services.subjects.dto.EventDto;
import lv.company.edup.services.subjects.dto.SubjectDto;
import lv.company.odata.api.ODataCriteria;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
    public Response createSubject(@Validate SubjectDto dto) {
        return created(subjectsService.createSubject(dto));
    }

    public Response searchSubjectEvents() {
        ODataCriteria criteria = new ODataCriteria(utils.getQueryParameters());
        return ok(subjectsService.searchSubjectEvents(criteria));
    }

    @Validated
    public Response createSubjectEvent(@Validate EventDto dto, Long subjectId) {
        return created(subjectsService.createSubjectEvent(dto, subjectId));
    }

    public Response getEventAttendance(Long subjectId, Long eventId) {
        return ok(subjectsService.getEventAttendance(subjectId, eventId));
    }
}
