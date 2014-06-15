package org.asuki.common.cdi.interceptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.asuki.common.annotation.TimeLog;
import org.slf4j.Logger;

import com.google.common.base.Stopwatch;

@TimeLog
@Interceptor
public class CustomInterceptor {

    @Inject
    private Logger log;

    @AroundInvoke
    public Object watchExecutionTime(InvocationContext ic) throws Exception {

        Object result = null;

        Stopwatch stopwatch = new Stopwatch().start();

        result = ic.proceed();

        stopwatch.stop();
        log.info("Execution time: {}", stopwatch);

        return result;
    }

    @PostConstruct
    public void aroundInit(InvocationContext ic) throws Exception {
        log.info("Before init " + ic.getTarget());

        ic.proceed();

        log.info("After init" + ic.getTarget());
    }

    @PreDestroy
    public void aroundDestroy(InvocationContext ic) throws Exception {
        log.info("Before destroy " + ic.getTarget());

        ic.proceed();

        log.info("After destroy " + ic.getTarget());
    }

}
