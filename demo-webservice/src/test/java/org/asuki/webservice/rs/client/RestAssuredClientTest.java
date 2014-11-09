package org.asuki.webservice.rs.client;

import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.http.ContentType.JSON;

import org.asuki.webservice.rs.model.User;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;

public class RestAssuredClientTest {

    @Test
    public void shouldPostUser() {
        User request = new User();
        request.setId(2);

        // @formatter:off
        Response response = given()
                                .body(request)
                                .contentType(JSON)
                                .log().all()
                            .expect()
                                .contentType(JSON)
                                .statusCode(OK.getStatusCode())
                                .log().body()
                            .when()
                                .post("http://localhost:8080/demo-web/rs/users");
        // @formatter:on

        User user = response.as(User.class);

        assertThat(
                user.toString(),
                startsWith("User(id=2, uri=/users/2, firstName=Tom, lastName=R"));
    }
}
