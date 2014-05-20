package org.asuki.service.impl;

import static org.asuki.model.entity.Address.builder;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asuki.dao.AddressDao;
import org.asuki.model.entity.Address;
import org.asuki.service.BootstrapService;

@Stateless(name = "BootstrapService")
@Local(BootstrapService.class)
public class BootstrapServiceImpl implements BootstrapService {

    @Inject
    private AddressDao addressDao;

    @Override
    public void initializeDatabase() {

        for (Address address : createAddresses()) {
            addressDao.create(address);
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
