package org.asuki.service.impl;

import java.util.List;

import org.asuki.dao.AddressDao;
import org.asuki.dao.BaseDao;
import org.asuki.model.entity.Address;
import org.asuki.service.AddressService;
import org.slf4j.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless(name = "AddressService")
@Remote(AddressService.class)
public class AddressServiceImpl extends BaseServiceImpl<Address, Integer>
        implements AddressService {

    @Inject
    private AddressDao addressDao;

    @Inject
    private Logger log;

    @Override
    public List<Address> findAll() {
        return addressDao.findAll();
    }

    @Override
    protected BaseDao<Address, Integer> getDao() {
        return addressDao;
    }

}
