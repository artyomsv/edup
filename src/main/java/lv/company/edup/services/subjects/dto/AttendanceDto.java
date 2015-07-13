package lv.company.edup.services.subjects.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AttendanceDto {

    private Long attendanceId;
    private Long eventId;
    @NotNull
    private Long studentId;
    private String name;
    private String lastName;
    private Date created;
    private Date updated;
    private Boolean participated;
    private Boolean notified;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
