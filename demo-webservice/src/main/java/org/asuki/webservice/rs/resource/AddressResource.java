package org.asuki.webservice.rs.resource;

import java.util.List;

import javax.annotation.security.PermitAll;
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
import org.asuki.model.jackson.JsonConverters;
import org.asuki.service.AddressService;
import org.slf4j.Logger;

@Path("/address")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class AddressResource extends BaseResource {

    @Inject
    private Logger log;

    @Inject
    private AddressService addressService;

    @Path("/all")
    @GET
    public List<Address> getAllAddresses() {

        return addressService.findAll();
    }

    //TODO
    @PermitAll
    @Path("/get/{addressId}")
    @GET
    public Address getAddress(@PathParam("addressId") Integer addressId) {

        return addressService.findById(addressId);
    }

    @Path("/get")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getJsonByJackson() {

        Address address = addressService.findById(1);
        log.info(address.toString());

        return JsonConverters.toString(address);
    }

    @Path("/create")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Address createAddress(Address address) {

        return addressService.create(address);
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Address updateAddress(Address address) {

        return addressService.edit(address);
    }

    @Path("/delete/{addressId}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteAddress(@PathParam("addressId") Integer addressId) {

        addressService.delete(addressId);
        return "Delete success";
    }

}
