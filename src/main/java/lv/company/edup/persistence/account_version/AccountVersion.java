package lv.company.edup.persistence.account_version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ACCOUNT_VERSION")
@SequenceGenerator(sequenceName = "account_version_id_seq", name = AccountVersion.Sequence.SEQUENCE, allocationSize = 1)
public class AccountVersion {

    interface Sequence {
        String SEQUENCE = "sAccountVersion";
    }

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_FK")
    private Long accountId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "MAIL")
    private String mail;

    @Column(name = "CREATED")
    private Date created;


    public Long getId() {
        return id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("id", id)
                .append("accountId", accountId)
                .append("name", name)
                .append("lastName", lastName)
                .append("mail", mail)
                .append("created", created)
                .toString();
    }
}
