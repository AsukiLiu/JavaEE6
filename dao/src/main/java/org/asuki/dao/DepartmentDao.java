package org.asuki.dao;

import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Department;

@ApplicationScoped
public class DepartmentDao extends BaseDao<Department, Integer> {

    @Override
    protected Class<Department> getEntityClass() {
        return Department.class;
    }

}
