package org.asuki.dao;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.asuki.model.entity.Address;

@ApplicationScoped
public class AddressDao extends BaseDao<Address, Integer> {

    @PostConstruct
    protected void postConstruct() {
        setEntityClass(Address.class);
    }

    public List<Address> findAll() {

        return findAll("address.all");
    }

}
