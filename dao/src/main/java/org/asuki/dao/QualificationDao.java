package org.asuki.dao;

import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Qualification;

@ApplicationScoped
public class QualificationDao extends BaseDao<Qualification, Integer> {

    @Override
    protected Class<Qualification> getEntityClass() {
        return Qualification.class;
    }

}
