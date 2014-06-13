package org.asuki.webservice.rs.resource;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;

//http://localhost:8080/demo-web/rs/demo
@Path("/demo")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
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
    @Produces(MediaType.TEXT_PLAIN)
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
    @Produces(MediaType.TEXT_PLAIN)
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

    @GET
    @Path("csv")
    @Produces("text/csv")
    public String[][] getCsv() {

        String[] row = { "a", "bb", "ccc" };
        String[][] table = { row, row, row };

        return table;
    }

    // @formatter:off
    /*
    [
        {
            "Key1": "Value1",
            "Key2": "Value2",
            "Key3": "Value3"
        },
        {
            "KeyA": "ValueA",
            "KeyB": "ValueB",
            "KeyC": "ValueC"
        }
    ]
     */
    // @formatter:on
    @POST
    @Path("list")
    @Consumes({ APPLICATION_JSON })
    @Produces({ APPLICATION_JSON })
    public List<Map<String, String>> postList(List<Map<String, String>> input) {

        log.info(input.toString());

        Map<String, String> map = newLinkedHashMap();
        map.put("key1", "value1");
        map.put("key2", "value2");

        return asList(map, map);
    }

}
