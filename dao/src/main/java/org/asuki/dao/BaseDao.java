package org.asuki.dao;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Iterables.getFirst;
import static javax.persistence.LockModeType.PESSIMISTIC_WRITE;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

/**
 * @param <E>
 *            Entity
 * @param <K>
 *            Key
 */
public abstract class BaseDao<E extends Serializable, K extends Serializable> {

    @PersistenceContext
    private EntityManager em;

    protected abstract Class<E> getEntityClass();

    /**
     * @param <R>
     *            Result
     */
    protected interface Query<E, R> {
        CriteriaQuery<R> execute(CriteriaBuilder builder,
                CriteriaQuery<R> query, Root<E> root);
    }

    protected interface EntityQuery<E> extends Query<E, E> {
    }

    // -----------------------Query by Criteria API-----------------------

    protected E getSingleResult(EntityQuery<E> query) {
        return createQuery(query, getEntityClass()).getSingleResult();
    }

    protected <R> R getSingleResult(Query<E, R> query, Class<R> resultClass) {
        return createQuery(query, resultClass).getSingleResult();
    }

    protected List<E> getResultList(EntityQuery<E> query) {
        return createQuery(query, getEntityClass()).getResultList();
    }

    protected List<E> getResultList(EntityQuery<E> query, int firstResult,
            int maxResults) {

        return createQuery(query, getEntityClass()).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public Long countAll() {

        return getSingleResult(new Query<E, Long>() {
            @Override
            public CriteriaQuery<Long> execute(CriteriaBuilder builder,
                    CriteriaQuery<Long> query, Root<E> root) {

                return query.select(builder.count(root));
            }
        }, Long.class);
    }

    private <R> TypedQuery<R> createQuery(Query<E, R> query,
            Class<R> resultClass) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<R> criteriaQuery = criteriaBuilder
                .createQuery(resultClass);
        Root<E> root = criteriaQuery.from(getEntityClass());

        return em.createQuery(query.execute(criteriaBuilder, criteriaQuery,
                root));
    }

    // -----------------------Query by JPQL-----------------------

    public Long count(String queryName) {

        return count(queryName, Maps.<String, Object> newHashMap());
    }

    public Long count(String queryName, Map<String, Object> parameters) {

        TypedQuery<Long> query = em.createNamedQuery(queryName, Long.class);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getSingleResult();
    }

    public E findByKey(K key) {

        return em.find(getEntityClass(), key);
    }

    public Optional<E> getSingleResult(String queryName,
            Map<String, Object> parameters) {

        return fromNullable(getFirst(getResultList(queryName, parameters), null));
    }

    public List<E> getResultList(String queryName) {

        return getResultList(queryName, Maps.<String, Object> newHashMap());
    }

    public List<E> getResultList(String queryName,
            Map<String, Object> parameters) {

        TypedQuery<E> query = em.createNamedQuery(queryName, getEntityClass());

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

    // -----------------------Other-----------------------

    public void create(E entity) {

        em.persist(entity);
    }

    public E update(E entity) {

        return em.merge(entity);
    }

    public void delete(E entity) {

        em.remove(entity);
    }

    public void deleteByKey(K key) {

        em.remove(findByKey(key));
    }

    public void detach(E entity) {

        em.detach(entity);
    }

    public boolean contains(E entity) {

        return em.contains(entity);
    }

    public void refresh(E entity) {

        em.refresh(entity);
    }

    public void lock(E entity) {

        em.refresh(entity, PESSIMISTIC_WRITE);
    }

}
