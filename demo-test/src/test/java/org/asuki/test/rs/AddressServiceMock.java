package org.asuki.test.rs;

import static org.asuki.model.entity.Address.builder;

import java.util.List;

import javax.enterprise.inject.Alternative;

import org.asuki.model.entity.Address;
import org.asuki.service.AddressService;

@Alternative
public class AddressServiceMock implements AddressService {

    @Override
    public List<Address> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Address findById(Integer addressId) {

        // @formatter:off
        return builder()
                .city("city")
                .prefecture("prefecture")
                .zipCode("zipCode")
                .build();
        // @formatter:on
    }

    @Override
    public Address create(Address address) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Address edit(Address address) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Integer addressId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Address address) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void put(Address address) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<Address> getCache() {
        // TODO Auto-generated method stub
        return null;
    }

}
