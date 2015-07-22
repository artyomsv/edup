package lv.company.edup.persistence.subjects.view;

import lv.company.edup.persistence.subjects.EventStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "V_SUBJECT_EVENTS_DETAILS")
public class SubjectEventsDetails {

    @Id
    @Column(name = "SUBJECT_EVENT_ID")
    private Long eventId;

    @Column(name = "SUBJECT_ID")
    private Long subjectId;

    @Column(name = "SUBJECT_NAME")
    private String name;

    @Column(name = "EVENT_DATE")
    private Date eventDate;

    @Column(name = "EVENT_TIME_FROM")
    private Date from;

    @Column(name = "EVENT_TIME_TO")
    private Date to;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "EVENT_PRICE")
    private Long price;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}
