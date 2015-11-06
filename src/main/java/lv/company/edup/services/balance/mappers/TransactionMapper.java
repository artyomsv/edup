package lv.company.edup.services.balance.mappers;

import lv.company.edup.infrastructure.mapping.CustomMapper;
import lv.company.edup.persistence.balance.Transaction;
import lv.company.edup.services.balance.dto.StudentBalanceDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;

public class TransactionMapper implements CustomMapper {

    @Override
    public void register(MapperFactory factory) {
        factory.classMap(Transaction.class, StudentBalanceDto.class)
                .customize(new ma.glasnost.orika.CustomMapper<Transaction, StudentBalanceDto>() {
                    @Override
                    public void mapAtoB(Transaction transaction, StudentBalanceDto dto, MappingContext context) {
                        Long amount = transaction.getCredit() > 0L ? transaction.getCredit() * -1L: transaction.getDebit();
                        dto.setAmount(amount);
                    }
                })
                .field("description", "comments")
                .byDefault()
                .register();
    }

}
