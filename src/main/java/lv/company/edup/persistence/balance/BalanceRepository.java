package lv.company.edup.persistence.balance;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class BalanceRepository extends EdupRepository<Balance> {

    @Override
    public Class<Balance> getEntityClass() {
        return Balance.class;
    }

}
