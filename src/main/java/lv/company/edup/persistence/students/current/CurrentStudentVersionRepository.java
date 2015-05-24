package lv.company.edup.persistence.students.current;

import lv.company.edup.persistence.EdupRepository;
import lv.company.edup.persistence.students.version.StudentVersion;

import javax.ejb.Stateless;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;

@Stateless
public class CurrentStudentVersionRepository extends EdupRepository<CurrentStudentVersion> {

    @Override
    public Class<CurrentStudentVersion> getEntityClass() {
        return CurrentStudentVersion.class;
    }

    public Long getNextStudentId() {
        StoredProcedureQuery query = em.createNamedStoredProcedureQuery(StudentVersion.Procedure.GENERATE_USER_ID);
        boolean execute = query.execute();
        if (execute) {
            BigInteger integer = (BigInteger) query.getSingleResult();
            return integer.longValue();
        } else {
            return null;
        }
    }
}
