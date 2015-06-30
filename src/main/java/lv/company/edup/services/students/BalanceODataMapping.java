package lv.company.edup.services.students;

import lv.company.edup.persistence.balance.Balance;
import lv.company.edup.persistence.balance.Balance_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class BalanceODataMapping extends AbstractODataJPAMapping<Balance> {

    public BalanceODataMapping() {
        mapAttribute("Id").to(Balance_.id);
        mapAttribute("Amount").to(Balance_.amount);
        mapAttribute("Created").to(Balance_.created);
        mapAttribute("Updated").to(Balance_.updated);
        mapAttribute("Comment").to(Balance_.comments);
        mapAttribute("StudentId").to(Balance_.studentId);
        mapAttribute("Status").to(Balance_.status);
    }

    @Override
    public Class<Balance> getEntityType() {
        return Balance.class;
    }

    @Override
    public SingularAttribute<Balance, ?> getPrimaryKeyAttribute() {
        return Balance_.id;
    }

}
