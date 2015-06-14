package lv.company.edup.persistence.account;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class AccountRepository extends EdupRepository<Account> {

    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }
}
