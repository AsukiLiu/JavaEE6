package org.asuki.webservice.event;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;

import org.slf4j.Logger;

public class CdiConsumer extends BaseConsumer {

    @Getter(AccessLevel.PROTECTED)
    @Inject
    private Logger log;

    public void consumeEvent(
            @Observes(during = TransactionPhase.AFTER_COMPLETION) MyEvent myEvent) {

        consume(myEvent);
    }

}