package org.asuki.web.push;

import static java.lang.String.valueOf;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.push.PushContext;
import org.primefaces.push.PushContextFactory;

@Named
@SessionScoped
public class CounterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String CHANNEL_NAME = "/counter";

    @Getter
    @Setter
    private AtomicInteger count = new AtomicInteger();

    public String getChannelName() {
        return CHANNEL_NAME;
    }

    public void increment() {
        PushContext pushContext = PushContextFactory.getDefault()
                .getPushContext();
        pushContext.push(CHANNEL_NAME, valueOf(count.incrementAndGet()));
    }

}
