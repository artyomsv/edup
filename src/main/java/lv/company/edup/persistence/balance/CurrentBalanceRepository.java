package lv.company.edup.persistence.balance;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class CurrentBalanceRepository extends EdupRepository<CurrentBalance> {

    @Override
    public Class<CurrentBalance> getEntityClass() {
        return CurrentBalance.class;
    }

}
