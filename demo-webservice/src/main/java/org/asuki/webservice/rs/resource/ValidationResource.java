package org.asuki.webservice.rs.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.status;
import static javax.ws.rs.core.Response.Status.CREATED;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.asuki.webservice.rs.model.Person;
import org.jboss.resteasy.spi.validation.ValidateRequest;

//http://localhost:8080/demo-web/rs/validation
@Path("validation")
@Produces(APPLICATION_JSON)
@ValidateRequest
public class ValidationResource {

    private static final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();

    @GET
    public Collection<Person> getAll() {
        return persons.values();
    }

    @GET
    @Path("{id}")
    public Person getPerson(
            @Context HttpHeaders headers,
            @PathParam("id") @Pattern(regexp = "[0-9]+", message = "{person.id.pattern}") String id) {

        return persons.get(id);
    }

    @POST
    @Path("form")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response createPerson(
            @Context HttpHeaders headers,
            @FormParam("id") @Pattern(regexp = "[0-9]+", message = "{person.id.pattern}") String id,
            @FormParam("name") @Size(min = 2, max = 50, message = "{person.name.size}") String name) {

        Person person = new Person();
        person.setId(id);
        person.setName(name);
        persons.put(id, person);

        return status(CREATED).entity(person).build();
    }

    // {"id":102,"name":"bbb"}
    @POST
    @Path("json")
    @Consumes(APPLICATION_JSON)
    public Response createPerson(@Context HttpHeaders headers,
            @Valid Person person) {

        persons.put(person.getId(), person);

        return status(CREATED).entity(person).build();
    }
}
