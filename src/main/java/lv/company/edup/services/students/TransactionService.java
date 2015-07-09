package lv.company.edup.services.students;

import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.persistence.balance.CurrentBalance;
import lv.company.edup.persistence.balance.CurrentBalanceRepository;
import lv.company.edup.persistence.balance.CurrentBalance_;
import lv.company.edup.persistence.balance.Transaction;
import lv.company.edup.persistence.balance.TransactionRepository;
import lv.company.edup.services.students.dto.CurrentBalanceDto;
import lv.company.edup.services.students.dto.StudentBalanceDto;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;
import org.apache.commons.collections4.CollectionUtils;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.core.MultivaluedMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TransactionService {

    public static final String CASH = "D1";
    public static final String BANK = "D2";
    @Inject @JPA ODataSearchService searchService;
    @Inject UriUtils utils;
    @Inject CurrentBalanceRepository currentRepository;
    @Inject TransactionRepository repository;
    @Inject ObjectMapper mapper;
    @Inject TransactionTypeService typeService;

    public CurrentBalanceDto currentStudentBalance(Long id) {
        CurrentBalance balance = currentRepository.find(id);
        if (balance == null) {
            return new CurrentBalanceDto(id, 0L);
        } else {
            return mapper.map(balance, CurrentBalanceDto.class);
        }
    }

    public Collection<CurrentBalanceDto> currentStudentsBalance(Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        List<CurrentBalance> balances = currentRepository.findByAttribute(ids, CurrentBalance_.studentId);
        if (CollectionUtils.isEmpty(balances)) {
            List<CurrentBalanceDto> dtos = new ArrayList<CurrentBalanceDto>(ids.size());
            for (Long id : ids) {
                dtos.add(new CurrentBalanceDto(id, 0L));
            }
            return dtos;
        }

        List<CurrentBalanceDto> dtos = mapper.map(balances, CurrentBalanceDto.class);
        for (CurrentBalanceDto dto : dtos) {
            if (dto.getAmount() == null) {
                dto.setAmount(0L);
            }
        }

        return dtos;

    }

    public ODataResult<StudentBalanceDto> search() {
        MultivaluedMap<String, String> queryParameters = utils.getQueryParameters();
        ODataResult<Transaction> search = searchService.search(new ODataCriteria(queryParameters), Transaction.class);
        List<Transaction> values = search.getValues();
        return search.cloneFromValues(mapper.map(values, StudentBalanceDto.class));
    }

    public Long debitTransaction(StudentBalanceDto dto) {
        Transaction transaction = new Transaction();
        transaction.setDebit(dto.getAmount());
        transaction.setCredit(0L);
        transaction.setCreated(new Date());
        transaction.setDescription(dto.getComments());
        transaction.setType(dto.getCash() ? typeService.getType(CASH).getId() : typeService.getType(BANK).getId());
        transaction.setStudentId(dto.getStudentId());
        repository.persist(transaction);
        return transaction.getId();
    }

}
