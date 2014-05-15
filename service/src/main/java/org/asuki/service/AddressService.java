package org.asuki.service;

import java.util.List;

import org.asuki.model.entity.Address;

public interface AddressService {

    List<Address> findAll();

    Address findById(Integer addressId);

    Address create(Address address);

    Address edit(Address address);

    void delete(Integer addressId);

}
