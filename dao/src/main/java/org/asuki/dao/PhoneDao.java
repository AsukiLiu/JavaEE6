package org.asuki.dao;

import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Phone;

@ApplicationScoped
public class PhoneDao extends BaseDao<Phone, Integer> {

    @Override
    protected Class<Phone> getEntityClass() {
        return Phone.class;
    }

}
