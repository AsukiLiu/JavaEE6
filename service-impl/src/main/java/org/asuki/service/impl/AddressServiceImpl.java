package org.asuki.service.impl;

import java.util.List;

import org.asuki.dao.AddressDao;
import org.asuki.model.entity.Address;
import org.asuki.service.AddressService;
import org.asuki.service.ContextService;
import org.slf4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless(name = "AddressService")
@Remote(AddressService.class)
public class AddressServiceImpl implements AddressService {

    @Inject
    private AddressDao addressDao;

    @EJB
    private ContextService contextService;

    @Inject
    private Logger log;

    @Override
    public List<Address> findAll() {

        Address address = contextService.getContextData();
        log.info("Got contextData: " + address.toString());

        return addressDao.findAll();
    }

    @Override
    public Address findById(Integer addressId) {

        return addressDao.findByKey(addressId);
    }

    @Override
    public Address create(Address address) {

        addressDao.create(address);
        return address;
    }

    @Override
    public Address edit(Address address) {

        return addressDao.update(address);
    }

    @Override
    public void delete(Integer addressId) {

        addressDao.deleteByKey(addressId);
    }

}
