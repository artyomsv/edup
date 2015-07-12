package lv.company.edup.persistence.subjects;

import lv.company.edup.persistence.EdupRepository;
import lv.company.edup.persistence.subjects.domain.Attendance;

import javax.ejb.Stateless;

@Stateless
public class AttendanceRepository extends EdupRepository<Attendance> {

    @Override
    public Class<Attendance> getEntityClass() {
        return Attendance.class;
    }

}
