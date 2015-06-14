package lv.company.edup.persistence;

import lv.company.odata.api.SearchOperator;
import lv.company.odata.impl.jpa.CriteriaBuilderSession;
import lv.company.odata.impl.jpa.CriteriaOperations;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class EdupRepository<Entity> {

    @Inject protected EntityManager em;

    public abstract Class<Entity> getEntityClass();

    public <Entity> Entity find(Object primaryKey, Class<Entity> aClass) {
        return em.find(aClass, primaryKey);
    }

    public Entity find(Object primaryKey) {
        if (primaryKey == null) {
            return null;
        }
        return em.find(getEntityClass(), primaryKey);
    }

    public void persist(Entity entity) {
        em.persist(entity);
    }

    public void persist(Collection<Entity> collection) {
        for (Entity entity : collection) {
            em.persist(entity);
        }
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

    public <T> List<Entity> findByAttribute(T o, SingularAttribute<Entity, T> attribute) {
        if (o == null) {
            return Collections.emptyList();
        }
        return findByAttribute(Collections.singleton(o), attribute);
    }

    public <T> List<Entity> findByAttribute(Collection<T> values, SingularAttribute<Entity, T> attribute) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
        Root<Entity> from = criteriaQuery.from(getEntityClass());
        CriteriaBuilderSession<Entity> session = new CriteriaBuilderSession<Entity>(from, criteriaBuilder, new CriteriaOperations<Entity>());
        Predicate predicate = session.composePredicate(from, attribute, SearchOperator.equal(), new ArrayList<T>(values));
        criteriaQuery.select(from).where(predicate);
        return em.createQuery(criteriaQuery).getResultList();
    }

    public List<Entity> findByMultipleAttributes(Map<Collection<?>, SingularAttribute<Entity, ?>> pairs) {
        if (MapUtils.isEmpty(pairs)) {
            return Collections.emptyList();
        }

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
        Root<Entity> from = criteriaQuery.from(getEntityClass());
        CriteriaBuilderSession<Entity> session = new CriteriaBuilderSession<Entity>(from, criteriaBuilder, new CriteriaOperations<Entity>());

        List<Predicate> predicates = new ArrayList<Predicate>();
        for (Map.Entry<Collection<?>, SingularAttribute<Entity, ?>> entry : pairs.entrySet()) {
            predicates.add(session.composePredicate(from, entry.getValue(), SearchOperator.equal(), new ArrayList(entry.getKey())));
        }

        criteriaQuery.select(from).where(predicates.toArray(new Predicate[predicates.size()]));
        return em.createQuery(criteriaQuery).getResultList();

    }


}
