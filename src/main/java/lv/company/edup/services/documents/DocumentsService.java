package lv.company.edup.services.documents;

import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.persistence.documents.StudentDocument;
import lv.company.edup.persistence.documents.StudentDocumentDetails;
import lv.company.edup.persistence.documents.StudentDocumentsDetailsRepository;
import lv.company.edup.persistence.documents.StudentDocumentsRepository;
import lv.company.edup.services.documents.dto.DocumentStatus;
import lv.company.edup.services.documents.dto.StudentDocumentDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Date;

@ApplicationScoped
public class DocumentsService {

    @Inject UriUtils utils;

    @Inject ObjectMapper mapper;
    @Inject StudentDocumentsRepository documentsRepository;
    @Inject StudentDocumentsDetailsRepository documentsDetailsRepository;
    @Inject @JPA ODataSearchService searchService;

    public ODataResult<StudentDocumentDto> search() {
        ODataCriteria criteria = new ODataCriteria(utils.getQueryParameters());
        if (!criteria.isNotEmpty()) {
            criteria.setSearch("*");
        }

        ODataResult<StudentDocumentDetails> result = searchService.search(criteria, StudentDocumentDetails.class);

        return result.cloneFromValues(mapper.map(result.getValues(), StudentDocumentDto.class));
    }

    public Long createDocument(Long studentId, Long fileId) {
        StudentDocument entity = new StudentDocument();
        entity.setStudentId(studentId);
        entity.setFileId(fileId);
        entity.setStatus(DocumentStatus.SAVED);
        entity.setCreated(new Date());
        entity.setUpdated(new Date());
        documentsRepository.persist(entity);
        return entity.getId();
    }

}
