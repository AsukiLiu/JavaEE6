package org.asuki.service.impl;

import static org.asuki.model.entity.Address.builder;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.asuki.model.entity.Address;
import org.asuki.service.AddressService;
import org.asuki.service.BootstrapService;

@Stateless(name = "BootstrapService")
@Remote(BootstrapService.class)
public class BootstrapServiceImpl implements BootstrapService {

    @EJB
    private AddressService addressService;

    @Override
    public void initializeDatabase() {

        for (Address address : createAddresses()) {
            addressService.create(address);
        }
    }

    private Address[] createAddresses() {

        // @formatter:off
        Address[] addresses = {
                builder()
                    .city("city1")
                    .prefecture("prefecture1")
                    .zipCode("zipCode1")
                    .build(),
                builder()
                    .city("city2")
                    .prefecture("prefecture2")
                    .zipCode("zipCode2")
                    .build() };
        // @formatter:on

        return addresses;
    }

}
