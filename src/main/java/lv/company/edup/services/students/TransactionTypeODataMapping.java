package lv.company.edup.services.students;

import lv.company.edup.persistence.balance.TransactionType;
import lv.company.edup.persistence.balance.TransactionType_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class TransactionTypeODataMapping extends AbstractODataJPAMapping<TransactionType> {

    public TransactionTypeODataMapping() {
        mapAttribute("Id").to(TransactionType_.id);
        mapAttribute("Code").to(TransactionType_.code);
        mapAttribute("Description").to(TransactionType_.description);
    }

    @Override
    public Class<TransactionType> getEntityType() {
        return TransactionType.class;
    }

    @Override
    public SingularAttribute<TransactionType, ?> getPrimaryKeyAttribute() {
        return TransactionType_.id;
    }

}
