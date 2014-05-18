package org.asuki.web.rs.resource;

import static org.asuki.common.jackson.JsonUtil.jsonSerialize;
import static org.asuki.model.entity.Address.builder;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.asuki.model.entity.Address;
import org.slf4j.Logger;

/**
 * http://localhost:8080/demo-web/rs
 * 
 * @author Asuki
 * 
 */
@Path("/address")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class AddressResource extends BaseResource {

    @Inject
    private Logger log;

    // TODO
    // @EJB
    // private AddressService addressService;

    @Path("/get/{addressId}")
    @GET
    public Address getAddress(@PathParam("addressId") Integer addressId) {

        return createDummyAddress();
    }

    @Path("/get")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getJsonByJackson() {

        Address address = createDummyAddress();
        log.info(address.toString());

        return jsonSerialize(address);

    }

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Address createAddress(Address address) {

        return createDummyAddress();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String updateAddress(Address address) {

        return "Update success";
    }

    @Path("/delete/{addressId}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAddress(@PathParam("addressId") Integer addressId) {

        return "Delete success";
    }

    // TODO dummy
    private static Address createDummyAddress() {

        // @formatter:off
        return builder()
                .city("city")
                .prefecture("prefecture")
                .zipCode("zipCode")
                .build();
        // @formatter:on
    }

}
