package org.asuki.test.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import javax.ejb.EJB;

import org.asuki.model.entity.Employee;
import org.asuki.service.BootstrapService;
import org.asuki.service.EmployeeService;
import org.asuki.service.PhoneService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EmployeeServiceIT extends BaseServiceArquillian {

    @EJB
    private BootstrapService bootstrapService;

    @EJB
    private EmployeeService employeeService;

    @EJB
    private PhoneService phoneService;

    @BeforeMethod
    public void beforeMethod() throws Exception {

    }

    @Test
    public void testCrud() {
        bootstrapService.initializeDatabase();

        List<Employee> employees = employeeService.findAll();
        Employee employee = employees.get(0);

        assertThat(employee.getMonthlySalary(), is(2000));
        assertThat(employee.getAddress(), is(notNullValue()));
        assertThat(employee.getPhone(), is(notNullValue()));
        assertThat(employee.getJob(), is(notNullValue()));
        assertThat(employee.getEmails().size() > 0, is(true));

        employee.setMonthlySalary(employee.getMonthlySalary() + 1000);
        employeeService.edit(employee);

        employee = employeeService.findById(1);

        assertThat(employee.getMonthlySalary(), is(2000 + 1000));

        employeeService.delete(employee);
        // employeeService.delete(employee.getId());

        assertThat(employeeService.findAll().size(), is(0));
        assertThat(phoneService.findAll().size(), is(0));
    }

}
