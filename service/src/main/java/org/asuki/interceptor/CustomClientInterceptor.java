package org.asuki.interceptor;

import static org.asuki.common.Constants.Ejbs.CONTEXT_DATA_KEY;

import java.util.Map;

import org.jboss.ejb.client.EJBClientInterceptor;
import org.jboss.ejb.client.EJBClientInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomClientInterceptor implements EJBClientInterceptor {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handleInvocation(EJBClientInvocationContext context)
            throws Exception {

        log.info("handleInvocation");

        Map<String, Object> contextData = context.getContextData();

        contextData.put(CONTEXT_DATA_KEY, "dummy");

        context.sendRequest();
    }

    @Override
    public Object handleInvocationResult(EJBClientInvocationContext context)
            throws Exception {

        log.info("handleInvocationResult");

        return context.getResult();
    }
}
