package org.asuki.service;

import static org.asuki.model.entity.Address.builder;
import static org.asuki.service.ContextService.ContextData.PARAM;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import javax.transaction.TransactionSynchronizationRegistry;

import lombok.Getter;

import org.asuki.model.entity.Address;

@Stateless
@LocalBean
public class ContextService {

    @Resource
    private SessionContext sc;

    @Resource
    private TransactionSynchronizationRegistry tsr;

    @Interceptors(ContextInterceptor.class)
    public Address getContextDataBySc() {
        return (Address) sc.getContextData().get(PARAM.getKey());
    }

    @Interceptors(ContextInterceptor.class)
    public Address getContextDataByTsr() {
        return (Address) tsr.getResource(PARAM.getKey());
    }

    public static enum ContextData {
        PARAM("paramKey");

        @Getter
        private String key;

        ContextData(String key) {
            this.key = key;
        }

    }

    public static class ContextInterceptor {

        @Resource
        private SessionContext sc;

        @Resource
        private TransactionSynchronizationRegistry tsr;

        @AroundInvoke
        public Object intercept(InvocationContext ic) throws Exception {

            // Dummy data
            // @formatter:off
            Address address = builder()
                    .city("city1")
                    .prefecture("prefecture1")
                    .zipCode("zipCode1")
                    .build();
            // @formatter:on

            sc.getContextData().put(PARAM.getKey(), address);

            tsr.putResource(PARAM.getKey(), address);

            return ic.proceed();
        }
    }
}
