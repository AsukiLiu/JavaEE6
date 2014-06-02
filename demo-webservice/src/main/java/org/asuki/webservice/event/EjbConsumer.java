package org.asuki.webservice.event;

import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;

import org.asuki.webservice.event.annotation.Cdi;
import org.slf4j.Logger;

@Singleton
public class EjbConsumer extends BaseConsumer {

    @Getter(AccessLevel.PROTECTED)
    @Inject
    private Logger log;

    @Asynchronous
    @Lock(LockType.READ)
    public void consumeEvent(@Observes @Cdi MyEvent myEvent) {

        consume(myEvent);
    }

}