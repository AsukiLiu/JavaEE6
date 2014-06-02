package org.asuki.webservice.event;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;

public abstract class BaseProducer {

    @Inject
    private Event<MyEvent> events;

    protected abstract Logger getLog();

    protected void produce(int numberOfEvents) {

        for (int i = 0; i < numberOfEvents; i++) {

            MyEvent event = new MyEvent(i);

            getLog().info("Produced: " + event);

            events.fire(event);
        }
    }
}
