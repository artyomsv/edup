package lv.company.edup.persistence.subjects.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "subject_event_attendance")
@SequenceGenerator(name = Attendance.SEQUENCE, sequenceName = "subject_event_attendance_sequence", allocationSize = 1)
public class Attendance {

    public static final String SEQUENCE = "sSubjectEventAttendance";

    @Id
    @Column(name = "subject_event_attendance_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    private Long attendanceId;

    @Column(name = "subject_event_fk")
    private Long eventId;

    @Column(name = "student_fk")
    private Long studentId;

    @Column(name = "created")
    private Date created;

    @Column(name = "updated")
    private Date updated;

    @Column(name = "participated")
    private Boolean participated;

    @Column(name = "notified_absence")
    private Boolean notified;

    @Column(name = "balance_adjusted")
    private Boolean balanceAdjusted;

    public Boolean getBalanceAdjusted() {
        return balanceAdjusted;
    }

    public void setBalanceAdjusted(Boolean balanceAdjusted) {
        this.balanceAdjusted = balanceAdjusted;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    public Boolean getParticipated() {
        return participated;
    }

    public void setParticipated(Boolean participated) {
        this.participated = participated;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }
}
