package org.asuki.test.rs.resource;

import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.UriBuilder.fromUri;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.startsWith;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.http.ContentType.JSON;

import com.jayway.restassured.response.Response;

import java.net.URI;
import java.net.URL;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.asuki.test.BaseArquillian;
import org.asuki.webservice.rs.RestApplication;
import org.asuki.webservice.rs.model.User;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import org.testng.annotations.Test;

@RunAsClient
public class ResourceClientIT extends BaseArquillian {

    static {
        jarFilterRegexp += "|^/WEB-INF/lib/resteasy-.*\\.jar$";
    }

    private static final String APPLICATION_PATH = RestApplication.class
            .getAnnotation(ApplicationPath.class).value().substring(1);

    @ArquillianResource
    private URL deploymentUrl;

    @Test
    public void testAddressResource() throws Exception {

        String uri = deploymentUrl.toString() + APPLICATION_PATH
                + "/address/get/1";

        ClientRequest request = new ClientRequest(uri);
        request.header("Accept", MediaType.APPLICATION_XML);

        ClientResponse<String> response = request.get(String.class);

        assertThat(response.getStatus(), is(200));

        // @formatter:off
        String content = response.getEntity()
                .replaceAll("<\\?xml.*\\?>", "")
                .replaceAll("<createdTime>.*</createdTime>", "")
                .replaceAll("<updatedTime>.*</updatedTime>", "")
                .trim();
        // @formatter:on

        assertThat(
                content,
                is("<address city=\"city\"><zipCode>zipCode</zipCode><prefecture>prefecture</prefecture></address>"));
    }

    @Test(enabled = false)
    public void testDemoResource() throws Exception {

        String uri = deploymentUrl.toString() + APPLICATION_PATH
                + "/demo/position;latitude=37.12;longitude=165.26";

        ClientRequest request = new ClientRequest(uri);
        request.header("Accept", MediaType.TEXT_PLAIN);

        ClientResponse<String> response = request.get(String.class);

        assertThat(response.getStatus(), is(200));
        assertThat(response.getEntity(), is("37.12N 165.26E"));
    }

    @Test
    public void testUserResource() {
        User request = new User();
        request.setId(2);

        // @formatter:off
        Response response = given()
                                .body(request)
                                .contentType(JSON)
                            .expect()
                                .contentType(JSON)
                                .statusCode(OK.getStatusCode())
                            .when()
                                .post(buildUri("rs", "users"));
        // @formatter:on

        User user = response.as(User.class);

        assertThat(
                user.toString(),
                startsWith("User(id=2, uri=/users/2, firstName=Tom, lastName=R"));
    }

    private URI buildUri(String... paths) {
        UriBuilder builder = fromUri(deploymentUrl.toString());
        for (String path : paths) {
            builder.path(path);
        }
        return builder.build();
    }

}
