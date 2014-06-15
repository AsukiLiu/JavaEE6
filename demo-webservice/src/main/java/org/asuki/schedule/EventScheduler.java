package org.asuki.schedule;

import static lombok.AccessLevel.PRIVATE;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.enterprise.inject.spi.BeanManager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Singleton
@Lock(LockType.READ)
public class EventScheduler {

    @Resource
    private TimerService timerService;

    @Resource
    private BeanManager beanManager;

    public void scheduleEvent(ScheduleExpression schedule, Object event,
            Annotation... qualifiers) {

        timerService.createCalendarTimer(schedule, new TimerConfig(
                new EventConfig(event, qualifiers), false));
    }

    @Timeout
    private void timeout(Timer timer) {
        final EventConfig config = (EventConfig) timer.getInfo();

        beanManager.fireEvent(config.getEvent(), config.getQualifiers());
    }

    @AllArgsConstructor(access = PRIVATE)
    private final class EventConfig implements Serializable {

        private static final long serialVersionUID = 1L;

        @Getter
        private final Object event;

        @Getter
        private final Annotation[] qualifiers;

    }

}
