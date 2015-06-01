package lv.company.edup.persistence.balance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_STUDENT_BALANCE")
public class CurrentBalance {

    @Id
    @Column(name = "STUDENT_FK")
    private Long studentId;

    @Column(name = "sum")
    private Long amount;

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
