package org.asuki.webservice.rs.interceptor;

import static java.util.Arrays.asList;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;

import org.asuki.webservice.rs.data.DummyUserDatabase;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.jboss.resteasy.util.Base64;
import org.slf4j.Logger;

@Provider
@ServerInterceptor
public class SecurityInterceptor implements PreProcessInterceptor {

    @Inject
    private Logger log;

    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    // @formatter:off
    private static final ServerResponse RESPONSE_UNAUTHORIZED = new ServerResponse(
            UNAUTHORIZED.getReasonPhrase(), 
            UNAUTHORIZED.getStatusCode(), 
            new Headers<Object>());;
    private static final ServerResponse RESPONSE_FORBIDDEN = new ServerResponse(
            FORBIDDEN.getReasonPhrase(), 
            FORBIDDEN.getStatusCode(), 
            new Headers<Object>());;
    private static final ServerResponse RESPONSE_ERROR = new ServerResponse(
            INTERNAL_SERVER_ERROR.getReasonPhrase(), 
            INTERNAL_SERVER_ERROR.getStatusCode(), 
            new Headers<Object>());
    // @formatter:on

    @Override
    public ServerResponse preProcess(HttpRequest request,
            ResourceMethod methodInvoked) throws Failure,
            WebApplicationException {

        Method method = methodInvoked.getMethod();

        if (method.isAnnotationPresent(PermitAll.class)) {
            return null;
        }

        if (method.isAnnotationPresent(DenyAll.class)) {
            return RESPONSE_FORBIDDEN;
        }

        List<String> authorization = request.getHttpHeaders().getRequestHeader(
                AUTHORIZATION_PROPERTY);
        if (authorization == null || authorization.isEmpty()) {
            return RESPONSE_UNAUTHORIZED;
        }

        String encodedInfo = authorization.get(0).replaceFirst(
                AUTHENTICATION_SCHEME + " ", "");

        String usernameAndPassword = null;
        try {
            usernameAndPassword = new String(Base64.decode(encodedInfo));
        } catch (IOException e) {
            log.error(e.getMessage());
            return RESPONSE_ERROR;
        }

        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword,
                ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        log.info("User name:{}, Password:{}", username, password);

        if (method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method
                    .getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(
                    asList(rolesAnnotation.value()));

            if (!isUserAllowed(username, password, rolesSet)) {
                return RESPONSE_UNAUTHORIZED;
            }
        }

        return null;
    }

    private boolean isUserAllowed(String username, String password,
            Set<String> rolesSet) {

        String userRole = DummyUserDatabase.getUserRole(username, password);
        return rolesSet.contains(userRole);
    }

}
