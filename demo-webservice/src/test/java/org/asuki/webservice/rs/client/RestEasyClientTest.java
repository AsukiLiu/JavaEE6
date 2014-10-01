package org.asuki.webservice.rs.client;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.UriBuilder;

import org.asuki.webservice.rs.model.User;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientRequestFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.spi.interception.ClientExecutionContext;
import org.jboss.resteasy.spi.interception.ClientExecutionInterceptor;
import org.testng.annotations.Test;

public class RestEasyClientTest {

    @Test
    public void testParsing() throws Exception {
        ClientRequest cr = new ClientRequest(
                "http://localhost:8080/demo-web/rs/users/1");

        String result = cr.get(String.class).getEntity();
        assertThat(result.replace("\"", ""),
                startsWith("{id:1,uri:/users/1,firstName:Andy,lastName:L"));
        cr.clear();

        cr.queryParameter("id", 2);
        User user = cr.get(User.class).getEntity();

        assertThat(
                user.toString(),
                startsWith("User(id=2, uri=/users/2, firstName=Tom, lastName=R"));
    }

    @Test
    public void testProxy() throws Exception {
        ClientRequestFactory crf = new ClientRequestFactory(UriBuilder.fromUri(
                "http://localhost:8080/demo-web/rs").build());

        ProxyApi proxyApi = crf.createProxy(ProxyApi.class);
        assertThat(
                proxyApi.getUser(2).toString(),
                startsWith("User(id=2, uri=/users/2, firstName=Tom, lastName=R"));
    }

    @Path("users")
    private static interface ProxyApi {
        @GET
        @Path("{id}")
        public User getUser(@PathParam("id") int id);
    }

    @Test
    public void testInterceptor() throws Exception {
        ClientRequestFactory crf = new ClientRequestFactory(UriBuilder.fromUri(
                "http://localhost:8080/demo-web/rs").build());

        crf.getPrefixInterceptors().getExecutionInterceptorList()
                .add(new ClientExecutionInterceptor() {
                    @Override
                    public ClientResponse execute(ClientExecutionContext ctx)
                            throws Exception {

                        out.println(format("Start. [Method:%s URI:%s]", ctx
                                .getRequest().getHttpMethod(), ctx.getRequest()
                                .getUri()));

                        return ctx.proceed();
                    }
                });

        crf.getSuffixInterceptors().getExecutionInterceptorList()
                .add(new ClientExecutionInterceptor() {
                    @Override
                    public ClientResponse execute(ClientExecutionContext ctx)
                            throws Exception {

                        out.println(format("End. [Method:%s URI:%s]", ctx
                                .getRequest().getHttpMethod(), ctx.getRequest()
                                .getUri()));

                        return ctx.proceed();
                    }
                });

        ClientRequest cr = crf.createRelativeRequest("/users/1");

        cr.queryParameter("id", 2);

        assertThat(cr.get(String.class).getEntity().replace("\"", ""),
                startsWith("{id:2,uri:/users/2,firstName:Tom,lastName:R"));
    }

}
