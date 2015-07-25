package lv.company.edup.services.students;

import lv.company.edup.infrastructure.utils.builder.IndexBuilder;
import lv.company.edup.persistence.balance.TransactionType;
import lv.company.odata.api.ODataCriteria;
import lv.company.odata.api.ODataResult;
import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.JPA;
import org.apache.commons.collections4.Transformer;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Startup
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TransactionTypeService {

    Map<String, TransactionType> map = new HashMap<>();

    @Inject @JPA ODataSearchService searchService;

    @PostConstruct
    public void init() {
        rebuild();
    }

    @Lock(LockType.WRITE)
    public TransactionType getType(String code) {
        return map.get(code);
    }

    @Lock(LockType.READ)
    public void rebuild() {
        ODataCriteria criteria = new ODataCriteria();
        ODataResult<TransactionType> result = searchService.search(criteria, TransactionType.class);

        map = IndexBuilder.<String, TransactionType>get()
                .key(new Transformer<TransactionType, String>() {
                    @Override
                    public String transform(TransactionType input) {
                        return input.getCode();
                    }
                })
                .map(result.getValues());

    }


}
