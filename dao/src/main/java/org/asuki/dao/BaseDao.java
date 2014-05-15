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

import lombok.Setter;

import com.beust.jcommander.internal.Maps;
import com.google.common.base.Optional;

public abstract class BaseDao<E extends Serializable, K extends Serializable> {

    @PersistenceContext
    private EntityManager em;

    @Setter
    private Class<E> entityClass;

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

        return em.find(entityClass, key);
    }

    public Optional<E> findOne(String queryName, Map<String, Object> parameters) {

        return fromNullable(getFirst(findAll(queryName, parameters), null));
    }

    public List<E> findAll(String queryName) {

        return findAll(queryName, Maps.<String, Object> newHashMap());
    }

    public List<E> findAll(String queryName, Map<String, Object> parameters) {

        TypedQuery<E> query = em.createNamedQuery(queryName, entityClass);

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }

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
