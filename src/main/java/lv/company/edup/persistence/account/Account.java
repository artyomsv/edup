package lv.company.edup.persistence.account;

import lv.company.edup.persistence.account_version.AccountVersion;
import lv.company.edup.persistence.credential_version.CredentialVersion;
import lv.company.edup.persistence.user_role.UserRole;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCOUNT")
@SequenceGenerator(sequenceName = "account_id_seq", name = Account.Sequence.SEQUENCE, allocationSize = 1)
public class Account {

    interface Sequence {
        String SEQUENCE = "sAccount";
    }

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_VERSION_FK", referencedColumnName = "ID")
    private AccountVersion accountVersion;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREDENTIAL_VERSION_FK", referencedColumnName = "ID")
    private CredentialVersion credentialVersion;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "CREATED")
    private Date created;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_FK", referencedColumnName = "ID")
    private Set<UserRole> userRoles = new HashSet<>();

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public AccountVersion getAccountVersion() {
        return accountVersion;
    }

    public void setAccountVersion(AccountVersion accountVersion) {
        this.accountVersion = accountVersion;
    }

    public CredentialVersion getCredentialVersion() {
        return credentialVersion;
    }

    public void setCredentialVersion(CredentialVersion credentialVersion) {
        this.credentialVersion = credentialVersion;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
                .append("userName", userName)
                .append("accountVersion", accountVersion)
                .append("credentialVersion", credentialVersion)
                .append("status", status)
                .append("created", created)
                .toString();
    }
}
