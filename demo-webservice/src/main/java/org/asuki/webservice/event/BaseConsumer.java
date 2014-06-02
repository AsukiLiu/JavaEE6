package org.asuki.webservice.event;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

import org.slf4j.Logger;

public abstract class BaseConsumer {

    protected abstract Logger getLog();

    @SneakyThrows
    protected void consume(MyEvent myEvent) {

        TimeUnit.SECONDS.sleep(1);

        getLog().info("Consumed: " + myEvent);
    }
}
