package org.asuki.service;

import java.util.List;

import org.asuki.model.entity.Address;

public interface AddressService extends BaseService<Address, Integer> {

    void put(Address address);

    void delete();

    List<Address> getCache();
}
