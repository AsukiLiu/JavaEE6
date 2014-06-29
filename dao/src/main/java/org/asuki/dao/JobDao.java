package org.asuki.dao;

import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Job;

@ApplicationScoped
public class JobDao extends BaseDao<Job, Integer> {

    @Override
    protected Class<Job> getEntityClass() {
        return Job.class;
    }

}
