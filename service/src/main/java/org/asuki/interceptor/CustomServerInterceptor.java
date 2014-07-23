package org.asuki.interceptor;

import static org.asuki.common.Constants.Ejbs.CONTEXT_DATA_KEY;

import java.util.Map;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.asuki.common.annotation.TimeLog;
import org.slf4j.Logger;

@TimeLog
@Interceptor
public class CustomServerInterceptor {

    @Inject
    private Logger log;

    @AroundInvoke
    public Object doSomething(InvocationContext context) throws Exception {

        Map<String, Object> contextData = context.getContextData();

        Object obj = contextData.get(CONTEXT_DATA_KEY);

        log.info("Received: " + obj);

        return context.proceed();
    }

}
