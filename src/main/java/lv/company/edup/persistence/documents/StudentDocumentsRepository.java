package lv.company.edup.persistence.documents;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class StudentDocumentsRepository extends EdupRepository<StudentDocument> {

    @Override
    public Class<StudentDocument> getEntityClass() {
        return StudentDocument.class;
    }

}
