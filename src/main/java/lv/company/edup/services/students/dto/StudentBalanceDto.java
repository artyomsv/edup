package lv.company.edup.services.students.dto;

import lv.company.edup.persistence.balance.BalanceStatus;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class StudentBalanceDto {

    private Long id;
    @NotNull
    private Long studentId;
    @NotNull
    private Long amount;
    private String comments;
    private BalanceStatus status;
    private Date created;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public BalanceStatus getStatus() {
        return status;
    }

    public void setStatus(BalanceStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
