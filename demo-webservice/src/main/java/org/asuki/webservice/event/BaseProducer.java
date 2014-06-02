package org.asuki.webservice.event;

import javax.enterprise.event.Event;

import org.slf4j.Logger;

public abstract class BaseProducer {

    protected abstract Event<MyEvent> getEvents();

    protected abstract Logger getLog();

    protected void produce(int numberOfEvents) {

        for (int i = 0; i < numberOfEvents; i++) {

            MyEvent event = new MyEvent(i);

            getLog().info("Produced: " + event);

            getEvents().fire(event);
        }
    }
}
