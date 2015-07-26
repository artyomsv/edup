package lv.company.edup.persistence.subjects.view;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class EventAttendanceRepository extends EdupRepository<AttendanceView> {

    @Override
    public Class<AttendanceView> getEntityClass() {
        return AttendanceView.class;
    }

}
