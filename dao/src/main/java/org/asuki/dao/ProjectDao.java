package org.asuki.dao;

import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Project;

@ApplicationScoped
public class ProjectDao extends BaseDao<Project, Integer> {

    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }

}
