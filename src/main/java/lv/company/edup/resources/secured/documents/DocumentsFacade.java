package lv.company.edup.resources.secured.documents;

import lv.company.edup.resources.ApplicationFacade;
import lv.company.edup.services.documents.DocumentsService;
import lv.company.edup.services.documents.dto.StudentDocumentDto;
import lv.company.edup.services.files.FileService;
import lv.company.edup.services.students.StudentsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class DocumentsFacade extends ApplicationFacade {

    @Inject DocumentsService documentsService;
    @Inject FileService fileService;
    @Inject StudentsService studentsService;

    public Response search() {
        return ok(documentsService.search());
    }

    public Response createStudentDocument(StudentDocumentDto dto) {
        studentsService.fetchLeanStudent(dto.getStudentId());
        Long document = documentsService.createDocument(dto.getStudentId(), dto.getFileId());
        return created(document);
    }

}
