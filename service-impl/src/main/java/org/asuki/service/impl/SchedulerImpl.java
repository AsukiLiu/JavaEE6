package org.asuki.service.impl;

import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.asuki.service.Scheduler;
import org.slf4j.Logger;

@Singleton
public class SchedulerImpl implements Scheduler {

    @Inject
    private Logger log;

    @Resource
    private TimerService timerService;

    @Timeout
    public void scheduler(Timer timer) {
        log.info("Scheduler(HA Singleton): " + timer.getInfo());
    }

    @Override
    public void start(String info) {
        log.info("Start timer service");
        ScheduleExpression sexpr = new ScheduleExpression();
        sexpr.hour("*").minute("*").second("0/5");

        // persistent must be false (started by ServiceActivator)
        timerService.createCalendarTimer(sexpr, new TimerConfig(info, false));
    }

    @Override
    public void stop() {
        log.info("Stop all timer services");
        for (Timer timer : timerService.getTimers()) {
            log.trace("Stop timer: " + timer.getInfo());
            timer.cancel();
        }
    }
}
