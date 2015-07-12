package lv.company.edup.persistence.subjects;

import lv.company.edup.persistence.EdupRepository;
import lv.company.edup.persistence.subjects.domain.Subject;

import javax.ejb.Stateless;

@Stateless
public class SubjectRepository extends EdupRepository<Subject> {

    @Override
    public Class<Subject> getEntityClass() {
        return Subject.class;
    }

}
