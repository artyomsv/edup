package lv.company.edup.persistence;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class EdupRepository<Entity> {

    @Inject protected EntityManager em;

    public abstract Class<Entity> getEntityClass();

    public <Entity> Entity find(Object primaryKey, Class<Entity> aClass) {
        return em.find(aClass, primaryKey);
    }

    public Entity find(Object primaryKey) {
        return em.find(getEntityClass(), primaryKey);
    }

    public void persist(Entity entity) {
        em.persist(entity);
    }

    public void delete(Entity entity) {
        em.remove(entity);
    }

    public Entity update(Entity entity) {
        return em.merge(entity);
    }

    @SuppressWarnings("unchecked")
    public List<Entity> findAll() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Entity> query = builder.createQuery(getEntityClass());
        Root<Entity> from = query.from(getEntityClass());
        query.select(from);
        return em.createQuery(query).getResultList();
    }

}
