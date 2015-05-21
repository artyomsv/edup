package lv.company.edup.persistence.students.version;

import lv.company.edup.persistence.EdupRepository;
import lv.company.edup.persistence.students.Student;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Stateless
public class StudentVersionRepository extends EdupRepository<StudentVersion> {

    @Override
    public Class<StudentVersion> getEntityClass() {
        return StudentVersion.class;
    }

    public Collection<? extends Student> findVersions(Long id) {
        TypedQuery<StudentVersion> query = em.createNamedQuery(StudentVersion.Query.FIND_VERSIONS, StudentVersion.class);
        query.setParameter(Student.Parameters.ID, id);
        return query.getResultList();
    }

}
