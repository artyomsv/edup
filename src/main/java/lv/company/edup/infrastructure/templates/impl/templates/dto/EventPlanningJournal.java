package lv.company.edup.infrastructure.templates.impl.templates.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class EventPlanningJournal {

    private String eventName;
    private List<StudentData> students;
    private List<EventData> events;

    public EventPlanningJournal(String eventName, List<StudentData> students, List<EventData> events) {
        this.eventName = eventName;
        this.students = students;
        this.events = events;
    }

    public String getEventName() {
        return eventName;
    }

    public List<StudentData> getStudents() {
        return students;
    }

    public List<EventData> getEvents() {
        return events;
    }

}
