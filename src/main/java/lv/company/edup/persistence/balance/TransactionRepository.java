package lv.company.edup.persistence.balance;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Stateless;

@Stateless
public class TransactionRepository extends EdupRepository<Transaction> {

    @Override
    public Class<Transaction> getEntityClass() {
        return Transaction.class;
    }

}
