package lv.company.edup.persistence.subjects.view;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Date;

@Stateless
public class SubjectEventRepository extends EdupRepository<SubjectEvents> {

    @Override
    public Class<SubjectEvents> getEntityClass() {
        return SubjectEvents.class;
    }

    public Collection<SubjectEvents> findEvents(Long subjectId, Date from, Date to) {
        TypedQuery<SubjectEvents> query = em.createNamedQuery(SubjectEvents.Query.FIND_EVENTS_BY_SUBJECT_ID, SubjectEvents.class);

        query.setParameter(SubjectEvents.Parameters.ID, subjectId);
        query.setParameter(SubjectEvents.Parameters.FROM, from);
        query.setParameter(SubjectEvents.Parameters.TO, to);

        return query.getResultList();

    }
}
