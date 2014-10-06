package org.asuki.web.bean;

import static java.util.UUID.randomUUID;
import static org.asuki.model.entity.Address.builder;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import lombok.Getter;
import lombok.Setter;

import org.asuki.model.entity.Address;
import org.asuki.service.AddressService;

@Named
@RequestScoped
public class AddressBean {

    @Inject
    private AddressService addressService;

    private List<Address> cacheList;

    @Resource(lookup = "java:jboss/infinispan/container/singleton")
    private CacheContainer container;

    // @Resource(lookup="java:jboss/infinispan/cache/singleton/default")
    private Cache<String, Address> cache;

    @Setter
    @Getter
    private String zipCode;

    @Setter
    @Getter
    private String prefecture;

    @Setter
    @Getter
    private String city;

    @PostConstruct
    public void initCache() {
        cache = container.getCache();
        cacheList = new ArrayList<>();
    }

    public void save() {
        // @formatter:off
        Address address = builder()
                .city(city)
                .prefecture(prefecture)
                .zipCode(zipCode)
                .build();
        // @formatter:on

        // addressService.put(address);

        cache.put(generateKey(), address);
    }

    public void clear() {
        // addressService.delete();
        cache.clear();
    }

    public List<Address> getCacheList() {
        List<Address> dataList = new ArrayList<>();
        dataList.addAll(cache.values());
        return dataList;

        // return addressService.getCache();
    }

    private String generateKey() {
        return randomUUID().toString();
    }
}
