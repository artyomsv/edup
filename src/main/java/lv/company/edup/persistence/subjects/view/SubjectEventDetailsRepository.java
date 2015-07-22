package lv.company.edup.persistence.subjects.view;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class SubjectEventDetailsRepository extends EdupRepository<SubjectEventsDetails> {

    @Override
    public Class<SubjectEventsDetails> getEntityClass() {
        return SubjectEventsDetails.class;
    }

}
