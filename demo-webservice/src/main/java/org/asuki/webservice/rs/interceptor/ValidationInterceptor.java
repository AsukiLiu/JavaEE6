package org.asuki.webservice.rs.interceptor;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.lang.reflect.Method;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

@Provider
@ServerInterceptor
public class ValidationInterceptor implements PreProcessInterceptor,
        AcceptedByMethod {

    // @formatter:off
    private static final ServerResponse RESPONSE_UNAUTHORIZED = new ServerResponse(
            UNAUTHORIZED.getReasonPhrase(), 
            UNAUTHORIZED.getStatusCode(), 
            new Headers<Object>());
    private static final ServerResponse RESPONSE_FORBIDDEN = new ServerResponse(
            FORBIDDEN.getReasonPhrase(), 
            FORBIDDEN.getStatusCode(), 
            new Headers<Object>());
    // @formatter:on

    @SuppressWarnings("rawtypes")
    public boolean accept(Class c, Method m) {
        return m.getName().equals("clearAll");
    }

    public ServerResponse preProcess(HttpRequest request, ResourceMethod method)
            throws Failure, WebApplicationException {

        ServerResponse response = null;

        List<String> params = request.getFormParameters().get("username");
        if (params == null || params.isEmpty()) {
            response = RESPONSE_UNAUTHORIZED;
        } else if (!"admin".equals(params.get(0))) {
            response = RESPONSE_FORBIDDEN;
        }

        return response;
    }
}
