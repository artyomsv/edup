package lv.company.edup.services.documents;

import lv.company.edup.persistence.documents.StudentDocument;
import lv.company.edup.persistence.documents.StudentDocument_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class DocumentsODataMapping extends AbstractODataJPAMapping<StudentDocument> {

    public DocumentsODataMapping() {
        mapAttribute("Id").to(StudentDocument_.id);
        mapAttribute("FileId").to(StudentDocument_.fileId);
        mapAttribute("StudentId").to(StudentDocument_.studentId);
        mapAttribute("Status").to(StudentDocument_.status);
        mapAttribute("Created").to(StudentDocument_.created);
        mapAttribute("Updated").to(StudentDocument_.updated);
    }

    @Override
    public Class<StudentDocument> getEntityType() {
        return StudentDocument.class;
    }

    @Override
    public SingularAttribute<StudentDocument, ?> getPrimaryKeyAttribute() {
        return StudentDocument_.id;
    }

}
