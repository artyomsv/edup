package lv.company.edup.resources.secured.students;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.validation.Validated;
import lv.company.edup.persistence.EntityPayload;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.TransactionService;
import lv.company.edup.services.students.dto.CurrentBalanceDto;
import lv.company.edup.services.students.dto.StudentDto;
import lv.company.edup.services.students.validation.UpdateCheck;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.groups.Default;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class StudentsFacade extends ApplicationFacade {

    private final Logger logger = Logger.getLogger(StudentsFacade.class.getSimpleName());

    @Inject StudentsService studentsService;
    @Inject TransactionService transactionService;

    public Response search() {
        return ok(studentsService.search());
    }

    public Response findStudent(Long id) {
        StudentDto student = studentsService.findStudent(id);
        fetchStudentBalance(id, student);
        return ok(student);
    }

    public Response findVersions(Long id) {
        return ok(studentsService.findVersions(id));
    }

    public Response findVersion(Long id, Long versionId) {
        return ok(studentsService.findVersion(id, versionId));
    }

    @Validated
    public Response createStudent(@Valid StudentDto dto) {
        EntityPayload payload = studentsService.createStudentVersion(dto);
        studentsService.updateIndex(payload.getId());
        return created(payload.getId(), payload.getVersionId());
    }

    @Validated(groups = {Default.class, UpdateCheck.class})
    public Response updateStudent(@Valid StudentDto dto, Long id, String etag) {
        if (StringUtils.isBlank(etag)) {
            throw new BadRequestException("Missing etag");
        }

        EntityPayload payload = studentsService.updateStudent(dto, id, etag);
        studentsService.updateIndex(payload.getId());
        return updated(payload.getVersionId());
    }

    public Response deleteStudent(Long id) {
        logger.log(Level.INFO, "delete student {0} is not supported yet", id);
        return ok();
    }

    public Response getStudentBalance(Long id) {
        if (studentsService.fetchLeanStudent(id) == null) {
            throw new NotFoundException("Missing student with [" + id + "] id.");
        }
        return ok(transactionService.currentStudentBalance(id));
    }

    private void fetchStudentBalance(Long id, StudentDto student) {
        if (student != null) {
            CurrentBalanceDto balance = transactionService.currentStudentBalance(id);
            student.setBalance(balance != null ? balance.getAmount() : 0L);
        }
    }
}
