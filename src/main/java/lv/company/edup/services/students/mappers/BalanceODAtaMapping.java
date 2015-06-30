package lv.company.edup.services.students.mappers;

import lv.company.edup.persistence.balance.Balance;
import lv.company.edup.persistence.balance.Balance_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class BalanceODAtaMapping extends AbstractODataJPAMapping<Balance> {

    public BalanceODAtaMapping() {
        mapAttribute("Id").to(Balance_.id);
        mapAttribute("Amount").to(Balance_.amount);
        mapAttribute("Created").to(Balance_.created);
        mapAttribute("Status").to(Balance_.status);
        mapAttribute("StudentId").to(Balance_.studentId);
        mapAttribute("Comments").to(Balance_.comments);
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
