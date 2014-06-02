package org.asuki.webservice.event;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import lombok.AccessLevel;
import lombok.Getter;

import org.asuki.webservice.event.annotation.Cdi;
import org.slf4j.Logger;

@Path("/produce")
@Getter(AccessLevel.PROTECTED)
public class CdiProducer extends BaseProducer {

    @Inject
    private Logger log;

    @Inject
    @Cdi
    private Event<MyEvent> events;

    @Path("/cdi/{number}")
    @GET
    public void produceEvents(@PathParam("number") int numberOfEvents) {

        produce(numberOfEvents);
    }

}
