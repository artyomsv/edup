package lv.company.edup.persistence.balance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "STUDENT_BALANCE")
@SequenceGenerator(sequenceName = "student_balance_sequence", name = Balance.SEQUENCE, allocationSize = 1)
public class Balance {

    public static final String SEQUENCE = "sBalance";

    @Id
    @GeneratedValue(generator = SEQUENCE, strategy = GenerationType.SEQUENCE)
    @Column(name = "balance_id")
    private Long id;

    @Column(name = "STUDENT_FK")
    private Long studentId;

    @Column(name = "AMOUNT")
    private Long amount;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BalanceStatus status;

    @Column(name = "CREATED")
    private Date created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
