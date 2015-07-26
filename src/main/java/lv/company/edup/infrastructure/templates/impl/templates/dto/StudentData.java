package lv.company.edup.infrastructure.templates.impl.templates.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class StudentData {

    private Long yCoordinate;
    private Long order;
    private String fullName;
    private List<EventData> studentEventData = new ArrayList<>();

    public StudentData(Long yCoordinate, Long order, String fullName) {
        this.yCoordinate = yCoordinate;
        this.order = order;
        this.fullName = fullName;
    }

    public StudentData(Long yCoordinate, Long order, String fullName, List<EventData> studentEventData) {
        this.yCoordinate = yCoordinate;
        this.order = order;
        this.fullName = fullName;
        this.studentEventData = studentEventData;
    }

    public Long getOrder() {
        return order;
    }

    public String getFullName() {
        return fullName;
    }

    public List<EventData> getStudentEventData() {
        return studentEventData;
    }

    public Long getyCoordinate() {
        return yCoordinate;
    }
}
