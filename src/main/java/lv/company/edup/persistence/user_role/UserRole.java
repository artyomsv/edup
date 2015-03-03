package lv.company.edup.persistence.user_role;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_ROLE")
@SequenceGenerator(sequenceName = "user_role_id_seq", name = UserRole.Sequence.SEQUENCE, allocationSize = 1)
public class UserRole {

    interface Sequence {
        String SEQUENCE = "sUserRole";
    }

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_FK")
    private Long accountId;

    @Column(name = "ROLE")
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
                .append("role", role)
                .append("created", created)
                .toString();
    }

}
