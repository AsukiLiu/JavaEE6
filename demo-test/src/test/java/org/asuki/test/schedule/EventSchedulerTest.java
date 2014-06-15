package org.asuki.test.schedule;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.ejb.AccessTimeout;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.enterprise.event.Observes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.asuki.schedule.EventScheduler;
import org.asuki.test.BaseArquillian;
import org.testng.annotations.Test;

public class EventSchedulerTest extends BaseArquillian {

    public static final CountDownLatch events = new CountDownLatch(3);

    @EJB
    private EventScheduler scheduler;

    @Test
    public void test() throws Exception {

        final ScheduleExpression schedule = new ScheduleExpression().hour("*")
                .minute("*").second("*/5");

        scheduler.scheduleEvent(schedule, new TestEvent("message"));

        assertThat(events.await(1, TimeUnit.MINUTES), is(true));
    }

    @AccessTimeout(value = 1, unit = TimeUnit.MINUTES)
    public void observe(@Observes TestEvent event) {

        if ("message".equals(event.getMessage())) {
            events.countDown();
        }
    }

    @AllArgsConstructor
    public static class TestEvent {

        @Getter
        private final String message;

    }

}
