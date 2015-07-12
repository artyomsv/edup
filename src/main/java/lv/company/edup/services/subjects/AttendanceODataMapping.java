package lv.company.edup.services.subjects;

import lv.company.edup.persistence.subjects.view.AttendanceView;
import lv.company.edup.persistence.subjects.view.AttendanceView_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class AttendanceODataMapping extends AbstractODataJPAMapping<AttendanceView> {

    public AttendanceODataMapping() {
        mapAttribute("Id").to(AttendanceView_.attendanceId);
        mapAttribute("EventId").to(AttendanceView_.eventId);
        mapAttribute("StudentId").to(AttendanceView_.studentId);
        mapAttribute("Created").to(AttendanceView_.created);
        mapAttribute("Updated").to(AttendanceView_.updated);
        mapAttribute("BalanceAdjusted").to(AttendanceView_.balanceAdjusted);
        mapAttribute("Notified").to(AttendanceView_.notified);
        mapAttribute("Participated").to(AttendanceView_.participated);
    }

    @Override
    public Class<AttendanceView> getEntityType() {
        return AttendanceView.class;
    }

    @Override
    public SingularAttribute<AttendanceView, ?> getPrimaryKeyAttribute() {
        return AttendanceView_.eventId;
    }

}
