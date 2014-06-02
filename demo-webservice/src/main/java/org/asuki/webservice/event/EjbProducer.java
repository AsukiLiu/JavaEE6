package org.asuki.webservice.event;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import lombok.AccessLevel;
import lombok.Getter;

import org.asuki.webservice.event.annotation.Ejb;
import org.slf4j.Logger;

@Stateless
@Path("/produce")
@Getter(AccessLevel.PROTECTED)
public class EjbProducer extends BaseProducer {

    @Inject
    private Logger log;

    @Inject
    @Ejb
    private Event<MyEvent> events;

    @Path("/ejb/{number}")
    @GET
    public void produceEvents(@PathParam("number") int numberOfEvents) {

        produce(numberOfEvents);
    }

}