package lv.company.edup.persistence.account.credential_version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "CREDENTIAL_VERSION")
@SequenceGenerator(sequenceName = "credential_version_id_seq", name = CredentialVersion.Sequence.SEQUENCE, allocationSize = 1)
public class CredentialVersion {

    interface Sequence {
        String SEQUENCE = "sCredentialVersion";
    }

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_FK")
    private Long accountId;

    @Column(name = "PASSWORD")
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
                .append("password", password)
                .append("created", created)
                .toString();
    }
}
