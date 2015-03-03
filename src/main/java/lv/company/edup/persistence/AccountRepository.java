package lv.company.edup.persistence;

import lv.company.edup.persistence.account.Account;

import javax.ejb.Stateless;

@Stateless
public class AccountRepository extends EdupRepository<Account> {

    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }
}
