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

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.asuki.model.entity.Address;

@Stateless
@LocalBean
public class ContextService {

    @Resource
    private SessionContext ctx;

    @Interceptors(ContextInterceptor.class)
    public Address getContextData() {

        return (Address) ctx.getContextData().get(PARAM.getKey());
    }

    @AllArgsConstructor
    public static enum ContextData {
        PARAM("paramKey");

        @Getter
        private String key;

    }

    public static class ContextInterceptor {

        @Resource
        private SessionContext ctx;

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

            ctx.getContextData().put(PARAM.getKey(), address);

            return ic.proceed();
        }
    }
}
