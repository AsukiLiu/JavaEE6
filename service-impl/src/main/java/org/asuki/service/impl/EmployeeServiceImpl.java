package org.asuki.service.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asuki.dao.BaseDao;
import org.asuki.dao.EmployeeDao;
import org.asuki.model.entity.Employee;
import org.asuki.service.EmployeeService;

@Stateless(name = "EmployeeService")
@Remote(EmployeeService.class)
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, Integer>
        implements EmployeeService {

    @Inject
    private EmployeeDao employeeDao;

    @Override
    protected BaseDao<Employee, Integer> getDao() {
        return employeeDao;
    }

}
