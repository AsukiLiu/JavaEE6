package org.asuki.web;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.asuki.common.Constants.Services;
import org.asuki.service.AccessService;
import org.asuki.service.AddressService;
import org.asuki.service.EmployeeService;

public class Resources {

    @Produces
    @EJB(lookup = Services.ADDRESS_SERVICE)
    private AddressService addressService;

    @Produces
    @EJB(lookup = Services.EMPLOYEE_SERVICE)
    private EmployeeService employeeService;

    @Produces
    @EJB(lookup = Services.ACCESS_SERVICE)
    private AccessService accessService;

    @Named
    @Produces
    @RequestScoped
    public FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
