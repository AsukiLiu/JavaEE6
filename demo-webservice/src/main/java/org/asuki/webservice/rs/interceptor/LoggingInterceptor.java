package org.asuki.webservice.rs.interceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.slf4j.Logger;

@Provider
@ServerInterceptor
public class LoggingInterceptor implements PreProcessInterceptor {

    @Inject
    private Logger log;

    @Context
    private HttpServletRequest servletRequest;

    public ServerResponse preProcess(HttpRequest request,
            ResourceMethod resourceMethod) throws Failure,
            WebApplicationException {

        String methodName = resourceMethod.getMethod().getName();

        log.info("Remote address:{}", servletRequest.getRemoteAddr());
        log.info("Remote user:{}", servletRequest.getRemoteUser());
        log.info("Method name:{}", methodName);

        return null;
    }

}