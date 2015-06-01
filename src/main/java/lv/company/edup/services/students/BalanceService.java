package lv.company.edup.services.students;

import lv.company.edup.infrastructure.exceptions.BadRequestException;
import lv.company.edup.infrastructure.exceptions.NotFoundException;
import lv.company.edup.infrastructure.mapping.ObjectMapper;
import lv.company.edup.infrastructure.response.UriUtils;
import lv.company.edup.persistence.balance.Balance;
import lv.company.edup.persistence.balance.BalanceRepository;
import lv.company.edup.persistence.balance.BalanceStatus;
import lv.company.edup.persistence.balance.CurrentBalance;
import lv.company.edup.persistence.balance.CurrentBalanceRepository;
import lv.company.edup.persistence.balance.CurrentBalance_;
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
public class BalanceService {

    @Inject @JPA ODataSearchService searchService;
    @Inject UriUtils utils;
    @Inject CurrentBalanceRepository currentRepository;
    @Inject BalanceRepository repository;
    @Inject ObjectMapper mapper;

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
        ODataResult<Balance> search = searchService.search(new ODataCriteria(queryParameters), Balance.class);
        List<Balance> values = search.getValues();
        return search.cloneFromValues(mapper.map(values, StudentBalanceDto.class));
    }

    public Long save(StudentBalanceDto dto) {
        Balance balance = new Balance();
        balance.setAmount(dto.getAmount());
        balance.setCreated(new Date());
        balance.setUpdated(new Date());
        balance.setComments(dto.getComments());
        balance.setStatus(BalanceStatus.SUBMITTED);
        balance.setStudentId(dto.getStudentId());
        repository.persist(balance);
        return balance.getId();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deactivateRecord(Long id) {
        Balance balance = repository.find(id);
        if (balance == null) {
            throw new NotFoundException("Missing balance record with id [" + id + "]");
        }

        if (balance.getStatus() == BalanceStatus.DELETED) {
            throw new BadRequestException("Balance record already deleted");
        }
        balance.setUpdated(new Date());
        balance.setStatus(BalanceStatus.DELETED);
    }
}
