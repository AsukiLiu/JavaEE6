package org.asuki.test.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.ejb.EJB;

import org.asuki.model.entity.Address;
import org.asuki.service.ContextService;
import org.testng.annotations.Test;

public class ContextServiceIT extends BaseServiceArquillian {

    @EJB
    private ContextService contextService;

    @Test
    public void testContext() {
        Address address1 = contextService.getContextDataBySc();

        Address address2 = contextService.getContextDataByTsr();

        assertThat(address1.toString(), is(address2.toString()));
    }

}
