package org.asuki.service.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.asuki.dao.BaseDao;
import org.asuki.dao.PhoneDao;
import org.asuki.model.entity.Phone;
import org.asuki.service.PhoneService;

@Stateless(name = "PhoneService")
@Remote(PhoneService.class)
public class PhoneServiceImpl extends BaseServiceImpl<Phone, Integer> implements
        PhoneService {

    @Inject
    private PhoneDao phoneDao;

    @Override
    protected BaseDao<Phone, Integer> getDao() {
        return phoneDao;
    }

}
