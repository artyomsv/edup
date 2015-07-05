package lv.company.edup.resources.secured.balance;

import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.documents.DocumentsService;
import lv.company.edup.services.students.BalanceService;
import lv.company.edup.services.students.StudentsService;
import lv.company.edup.services.students.dto.StudentBalanceDto;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class BalanceFacade extends ApplicationFacade {

    @Inject BalanceService balanceService;
    @Inject StudentsService studentsService;
    @Inject DocumentsService documentsService;

    public Response search() {
        return ok(balanceService.search());
    }

    public Response saveBalance(StudentBalanceDto dto) {
        Student student = studentsService.fetchLeanStudent(dto.getStudentId());
        if (student == null) {
            throw new NotFoundException("Missing student id");
        }

        Long save = balanceService.save(dto);
        if (dto.getCash()) {
            documentsService.addDocument(student, dto.getAmount(), dto.getComments());
        }

        return created(save);
    }

    public Response deactivateBalanceRecord(Long id) {
        balanceService.deactivateRecord(id);
        return ok();
    }
}
