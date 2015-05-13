package lv.company.edup.infrastructure.templates.impl.templates.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StudentDto {

    private String studentOrder;
    private String studentName;

    public StudentDto() {
    }

    public StudentDto(String studentOrder, String studentName) {
        this.studentOrder = studentOrder;
        this.studentName = studentName;
    }

    public String getStudentOrder() {
        return studentOrder;
    }

    public void setStudentOrder(String studentOrder) {
        this.studentOrder = studentOrder;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
