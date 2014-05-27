package org.asuki.web;

import javax.ejb.EJB;
import javax.enterprise.inject.Produces;

import org.asuki.common.Constants.Services;
import org.asuki.service.AddressService;

public class Resources {

    @Produces
    @EJB(lookup = Services.ADDRESS_SERVICE)
    private AddressService addressService;

}