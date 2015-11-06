package lv.company.edup.services.balance.odata;

import lv.company.edup.persistence.balance.Transaction;
import lv.company.edup.persistence.balance.Transaction_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class BalanceODAtaMapping extends AbstractODataJPAMapping<Transaction> {

    public BalanceODAtaMapping() {
        mapAttribute("Id").to(Transaction_.id);
        mapAttribute("Debit").to(Transaction_.debit);
        mapAttribute("Credit").to(Transaction_.credit);
        mapAttribute("Created").to(Transaction_.created);
        mapAttribute("Type").to(Transaction_.type);
        mapAttribute("StudentId").to(Transaction_.studentId);
        mapAttribute("Description").to(Transaction_.description);
    }

    @Override
    public Class<Transaction> getEntityType() {
        return Transaction.class;
    }

    @Override
    public SingularAttribute<Transaction, ?> getPrimaryKeyAttribute() {
        return Transaction_.id;
    }

}
