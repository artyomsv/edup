package lv.company.edup.persistence;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProvider {

    @PersistenceContext(name = "EDUP_PERSISTENCE")
    private EntityManager entityManager;

    @Produces
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
