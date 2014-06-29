package org.asuki.dao;

import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Email;

@ApplicationScoped
public class EmailDao extends BaseDao<Email, Integer> {

    @Override
    protected Class<Email> getEntityClass() {
        return Email.class;
    }

}
