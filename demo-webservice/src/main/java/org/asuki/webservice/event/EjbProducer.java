package org.asuki.webservice.event;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import lombok.AccessLevel;
import lombok.Getter;

import org.slf4j.Logger;

@Stateless
@Path("/produce")
public class EjbProducer extends BaseProducer {

    @Getter(AccessLevel.PROTECTED)
    @Inject
    private Logger log;

    @Path("/ejb/{number}")
    @GET
    public void produceEvents(@PathParam("number") int numberOfEvents) {

        produce(numberOfEvents);
    }

}