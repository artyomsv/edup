package lv.company.edup.persistence.documents;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class StudentDocumentsDetailsRepository extends EdupRepository<StudentDocumentDetails> {

    @Override
    public Class<StudentDocumentDetails> getEntityClass() {
        return StudentDocumentDetails.class;
    }

}
