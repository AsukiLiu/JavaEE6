package org.asuki.webservice.event;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import lombok.AccessLevel;
import lombok.Getter;

import org.slf4j.Logger;

@Path("/produce")
public class CdiProducer extends BaseProducer {

    @Getter(AccessLevel.PROTECTED)
    @Inject
    private Logger log;

    @Path("/cdi/{number}")
    @GET
    public void produceEvents(@PathParam("number") int numberOfEvents) {

        produce(numberOfEvents);
    }

}
