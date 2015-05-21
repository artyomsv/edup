package lv.company.edup.infrastructure.templates.impl.templates.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StudentTemplateData {

    private String studentOrder;
    private String studentName;

    public StudentTemplateData() {
    }

    public StudentTemplateData(String studentOrder, String studentName) {
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
