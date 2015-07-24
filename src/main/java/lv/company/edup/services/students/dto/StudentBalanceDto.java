package lv.company.edup.services.students.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentBalanceDto {

    private Long id;
    @NotNull @DecimalMin(inclusive = true, value = "0", message = "Student ID cannot be negative!")
    private Long studentId;
    @NotNull
    @DecimalMin(inclusive = false, value = "0", message = "Amount have to be grater then zero!")
    @DecimalMax(inclusive = true, value = "100000", message = "Maximum amount must not exceed 1000.00 EURO")
    private Long amount;
    @Size(max = 256, message = "Transaction comment must be between {min} and {max} characters long")
    private String comments;
    private Date created;
    private Boolean cash;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getCash() {
        return cash;
    }

    public void setCash(Boolean cash) {
        this.cash = cash;
    }
}
