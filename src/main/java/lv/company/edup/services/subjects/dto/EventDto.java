package lv.company.edup.services.subjects.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lv.company.edup.persistence.subjects.EventStatus;
import lv.company.edup.services.students.validation.UpdateCheck;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EventDto {

    @NotNull(groups = {UpdateCheck.class})
    private Long eventId;
    @NotNull(message = "Subject envelope cannot be null")
    @Valid
    private SubjectDto subject;
    @NotNull(message = "Event is missing event date")
    private Date eventDate;
    @NotNull(message = "Event is missing time from")
    private Date from;
    @NotNull(message = "Event is missing time to")
    private Date to;
    @NotNull(message = "Event is missing price")
    private Long price;
    private EventStatus status;
    private Long students;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public SubjectDto getSubject() {
        return subject;
    }

    public void setSubject(SubjectDto subject) {
        this.subject = subject;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Long getStudents() {
        return students;
    }

    public void setStudents(Long students) {
        this.students = students;
    }
}
