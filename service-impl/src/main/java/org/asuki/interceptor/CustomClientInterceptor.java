package org.asuki.interceptor;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;
import org.slf4j.Logger;

public class CustomClientInterceptor implements EJBClientInterceptor {

    @Inject
    private Logger log;

    @Override
    public void handleInvocation(EJBClientInvocationContext context)
            throws Exception {

        log.info("handleInvocation");

        Map<String, Object> contextData = context.getContextData();

        contextData.put("xKey", "dummy");

        context.sendRequest();
    }

    @Override
    public Object handleInvocationResult(EJBClientInvocationContext context)
            throws Exception {

        log.info("handleInvocationResult");

        return context.getResult();
    }
}
