package lv.company.edup.persistence.account_actions;

import lv.company.edup.persistence.account.Account;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ACCOUNT_ACTION")
@SequenceGenerator(sequenceName = "account_action_id_seq", name = AccountAction.Sequence.SEQUENCE, allocationSize = 1)
public class AccountAction {

    interface Sequence {
        String SEQUENCE = "sAccountAction";
    }

    @Id
    @Column(name = "ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_FK", referencedColumnName = "ID")
    private Account account;

    @Column(name = "IP")
    private String ip;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "TIME")
    private Date time;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ActionType type;

    @Column(name = "CONTEXT")
    private String context;

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getIp() {
        return ip;
    }

    public String getUuid() {
        return uuid;
    }

    public Date getTime() {
        return time;
    }

    public ActionType getType() {
        return type;
    }

    public String getContext() {
        return context;
    }
}
