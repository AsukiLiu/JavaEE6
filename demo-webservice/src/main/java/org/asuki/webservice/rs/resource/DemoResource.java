package org.asuki.webservice.rs.resource;

import static java.lang.String.format;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;

//http://localhost:8080/demo-web/rs/demo
@Path("/demo")
@Produces({ APPLICATION_JSON, APPLICATION_XML })
public class DemoResource extends BaseResource {

    @Inject
    private Logger log;

    @Path("/errmsg")
    @GET
    public String getErrorMessage() {

        throw new IllegalArgumentException("IllegalArgumentException throwed");
    }

    // user/a01/xxx@gmail.com/090-9999-9999
    @Path("user/{id: [a-zA-Z][a-zA-Z0-9]*}/{mail}/{phone}")
    @GET
    @Produces(TEXT_PLAIN)
    public String getUser(
            // @formatter:off
            @PathParam("id") String id,
            @PathParam("mail") String mail, 
            @PathParam("phone") String phone) {
            // @formatter:on

        Map<String, String> user = new HashMap<>();
        user.put("id", id);
        user.put("mail", mail);
        user.put("phone", phone);

        return user.toString();
    }

    // position;latitude=37.12;longitude=165.26
    // @formatter:off
    @Path("/position")
    @GET
    @Produces(TEXT_PLAIN)
    public String getPosition(
            @MatrixParam("latitude") double latitude,
            @MatrixParam("longitude") double longitude) {

        // 37.12N 165.26E
        return format("%3.2f%s %3.2f%s",
                Math.abs(latitude),
                latitude == 0.0 ? "" : (latitude > 0.0 ? "N" : "S"),
                Math.abs(longitude), 
                longitude == 0.0 ? "": (longitude > 0.0 ? "E" : "W"));
    }
    // @formatter:on

}
