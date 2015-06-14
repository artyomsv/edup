package lv.company.edup.services.documents;

import lv.company.edup.persistence.documents.StudentDocumentDetails;
import lv.company.edup.persistence.documents.StudentDocumentDetails_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class DocumentsDetailsODataMapping extends AbstractODataJPAMapping<StudentDocumentDetails> {

    public DocumentsDetailsODataMapping() {
        mapAttribute("Id").to(StudentDocumentDetails_.id);
        mapAttribute("FileId").to(StudentDocumentDetails_.fileId);
        mapAttribute("StudentId").to(StudentDocumentDetails_.studentId);
        mapAttribute("Created").to(StudentDocumentDetails_.created);
        mapAttribute("FileName").to(StudentDocumentDetails_.fileName);
        mapAttribute("Size").to(StudentDocumentDetails_.size);
    }

    @Override
    public Class<StudentDocumentDetails> getEntityType() {
        return StudentDocumentDetails.class;
    }

    @Override
    public SingularAttribute<StudentDocumentDetails, ?> getPrimaryKeyAttribute() {
        return StudentDocumentDetails_.id;
    }

}
