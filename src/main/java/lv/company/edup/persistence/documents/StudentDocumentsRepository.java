package lv.company.edup.persistence.documents;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;

@Stateless
public class StudentDocumentsRepository extends EdupRepository<StudentDocument> {

    @Override
    public Class<StudentDocument> getEntityClass() {
        return StudentDocument.class;
    }

    public Long getNextFakturaId() {
        StoredProcedureQuery query = em.createNamedStoredProcedureQuery(StudentDocument.Procedure.GENERATE_FAKTURA_ID);
        boolean execute = query.execute();
        if (execute) {
            BigInteger integer = (BigInteger) query.getSingleResult();
            return integer.longValue();
        } else {
            return null;
        }
    }

}
