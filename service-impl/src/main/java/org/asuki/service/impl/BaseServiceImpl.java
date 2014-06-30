package org.asuki.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

import org.asuki.dao.BaseDao;
import org.asuki.model.BaseEntity;
import org.asuki.service.BaseService;

public abstract class BaseServiceImpl<E extends BaseEntity, K extends Serializable>
        implements BaseService<E, K> {

    protected abstract BaseDao<E, K> getDao();

    @Resource
    protected SessionContext ctx;

    @Override
    public List<E> findAll() {
        return getDao().findAll();
    }

    @Override
    public E findById(K key) {
        return getDao().findByKey(key);
    }

    @Override
    public E create(E e) {
        getDao().create(e);
        return e;
    }

    @Override
    public E edit(E e) {
        return getDao().update(e);
    }

    @Override
    public void delete(E e) {
        e = getDao().update(e);
        getDao().delete(e);
    }

    @Override
    public void delete(K key) {
        getDao().deleteByKey(key);
    }

}
