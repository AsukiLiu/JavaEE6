package org.asuki.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Employee;

@ApplicationScoped
public class EmployeeDao extends BaseDao<Employee, Integer> {

    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }

    public List<Employee> findAll() {
        return getResultList("employee.all");
    }
    

}
