package lv.company.edup.persistence.balance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "STUDENT_TRANSACTIONS")
@SequenceGenerator(sequenceName = "student_transactions_sequence", name = Transaction.SEQUENCE, allocationSize = 1)
public class Transaction {

    public static final String SEQUENCE = "sTransactions";

    @Id
    @GeneratedValue(generator = SEQUENCE, strategy = GenerationType.SEQUENCE)
    @Column(name = "TRANSACTION_ID")
    private Long id;

    @Column(name = "STUDENT_FK")
    private Long studentId;

    @Column(name = "DEBIT")
    private Long debit;

    @Column(name = "CREDIT")
    private Long credit;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TRANSACTION_TYPE_FK")
    private Long type;

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

    public Long getDebit() {
        return debit;
    }

    public void setDebit(Long debit) {
        this.debit = debit;
    }

    public Long getCredit() {
        return credit;
    }

    public void setCredit(Long credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
