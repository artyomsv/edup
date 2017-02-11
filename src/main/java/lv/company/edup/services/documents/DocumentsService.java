package lv.company.edup.services.documents;

import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.infrastructure.templates.api.*;
import lv.company.edup.infrastructure.templates.impl.TemplateCache;
import lv.company.edup.infrastructure.templates.impl.templates.dto.FakturaData;
import lv.company.edup.infrastructure.time.AppTimeZone;
import lv.company.edup.infrastructure.utils.ApplicationUtils;
import lv.company.edup.persistence.documents.StudentDocument;
import lv.company.edup.persistence.documents.StudentDocumentDetails;
import lv.company.edup.persistence.documents.StudentDocumentsDetailsRepository;
import lv.company.edup.persistence.documents.StudentDocumentsRepository;
import lv.company.edup.persistence.files.FileType;
import lv.company.edup.persistence.students.Student;
import lv.company.edup.services.documents.dto.DocumentStatus;
import lv.company.edup.services.documents.dto.StudentDocumentDto;
import lv.company.edup.services.files.FileService;
import lv.company.edup.services.files.dto.FileDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Date;
import java.util.Map;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DocumentsService {

    @Inject UriUtils utils;

    @Inject ObjectMapper mapper;
    @Inject StudentDocumentsRepository documentsRepository;
    @Inject FileService fileService;
    @Inject StudentDocumentsDetailsRepository documentsDetailsRepository;
    @Inject ODataSearchService searchService;

    @Inject ContextCreator<FakturaData> fakturaContextCreator;
    @Inject @JasperEngine TemplateEngine templateEngine;
    @Inject TemplateCache cache;

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

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void addDocument(Student student, Long amount, String comments) {
        FakturaData faktura = new FakturaData();
        faktura.setPaymentDescription(comments);
        faktura.setPaymentTotal(amount);
        faktura.setPaymentId(documentsRepository.getNextFakturaId());
        Map<String, Object> context = fakturaContextCreator.create(faktura);

        byte[] data = templateEngine.render(cache.getTemplate(TemplateName.FakturaJasper), context, Type.PDF);

        FileDto fileDto = fileService.persistFile(data, getFakturaFileName(student, faktura.getPaymentId(), amount), FileType.PDF);

        StudentDocument entity = new StudentDocument();
        entity.setStatus(DocumentStatus.SAVED);
        entity.setStudentId(student.getId());
        entity.setFileId(fileDto.getId());
        entity.setCreated(new Date());
        entity.setUpdated(new Date());
        documentsRepository.persist(entity);
    }

    private String getFakturaFileName(Student student, Long paymentId, Long amount) {
        StringBuilder builder = new StringBuilder();
        builder.append(student.getName())
                .append("_")
                .append(student.getLastName())
                .append("_")
                .append(DateFormatUtils.format(new Date(), "yyyy_MM_dd", AppTimeZone.TIME_ZONE))
                .append("[NR")
                .append(paymentId)
                .append("][")
                .append(ApplicationUtils.getAmountValue(amount)).append("EUR")
                .append("]")
                .append(".pdf");
        return builder.toString();
    }

}
