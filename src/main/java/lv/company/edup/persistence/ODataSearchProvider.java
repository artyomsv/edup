package lv.company.edup.persistence;

import lv.company.odata.api.ODataSearchService;
import lv.company.odata.impl.ODataSearchServiceImpl;
import lv.company.odata.impl.jpa.ODataJPAMapping;
import lv.company.odata.impl.jpa.ODataPersistenceServiceImpl;
import lv.company.odata.impl.parse.QueryParser;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

public class ODataSearchProvider {

    @Inject QueryParser queryParser;
    @Inject EntityManager em;
    @Inject Instance<ODataJPAMapping<?>> mappings;


    @Produces
    public ODataSearchService service() {
        return new ODataSearchServiceImpl(queryParser, new ODataPersistenceServiceImpl(mappings) {
            @Override
            public EntityManager em() {
                return em;
            }
        });
    }
}
