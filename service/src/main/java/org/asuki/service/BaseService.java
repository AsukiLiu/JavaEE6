package org.asuki.service;

import java.io.Serializable;
import java.util.List;

import org.asuki.model.BaseEntity;

public interface BaseService<E extends BaseEntity, K extends Serializable> {

    List<E> findAll();

    E findById(K key);

    E create(E e);

    E edit(E e);

    void delete(E e);

    void delete(K key);

}
