package org.asuki.webservice.rs.resource;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static javax.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;

//http://localhost:8080/demo-web/rs/exception
@Path("exception")
@Produces(TEXT_PLAIN)
public class ExceptionResource {

    @Path("mapper")
    @GET
    public String getErrorMessage() {

        throw new IllegalArgumentException("IllegalArgumentException throwed");
    }

    @Path("error")
    @GET
    public String get503Status() {

        throw new WebApplicationException(SERVICE_UNAVAILABLE);
    }
}
