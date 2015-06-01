package lv.company.edup.services.students.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CurrentBalanceDto {

    private Long studentId;
    private Long amount;

    public CurrentBalanceDto() {
    }

    public CurrentBalanceDto(Long studentId, Long amount) {
        this.studentId = studentId;
        this.amount = amount;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
