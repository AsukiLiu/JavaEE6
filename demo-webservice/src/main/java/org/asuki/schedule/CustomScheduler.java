package org.asuki.schedule;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Schedule;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.ScheduleExpression;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import org.asuki.model.entity.Address;
import org.asuki.service.AddressService;
import org.slf4j.Logger;

@Singleton
@Startup
public class CustomScheduler {

    private static final String TIMER_NAME = "myTimer";

    @Inject
    private Logger log;

    @Inject
    private AddressService addressService;

    @Resource
    private TimerService timerService;

    // @formatter:off
    /*
    @Schedules({
            @Schedule(month = "5", dayOfMonth = "20-Last", minute = "0", hour = "8"),
            @Schedule(month = "6", dayOfMonth = "1-10", minute = "0", hour = "8") })
    */
    // @formatter:on
    // per 5 seconds
    @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
    public void schedule() {
        serviceLogic();
    }

    @PostConstruct
    public void changeSchedule() {

        Collection<Timer> timers = timerService.getTimers();
        for (Timer timer : timers) {
            timer.cancel();
        }

        ScheduleExpression expression = new ScheduleExpression();
        expression.second("*/10").minute("*").hour("*");
        // expression.month(5).dayOfMonth("20-Last").minute(0).hour(8);

        final TimerConfig config = new TimerConfig(TIMER_NAME, false);
        timerService.createCalendarTimer(expression, config);
    }

    @Timeout
    public void timeout(Timer timer) {
        if (TIMER_NAME.equals(timer.getInfo())) {
            serviceLogic();
        }
    }

    private void serviceLogic() {
        Address address = addressService.findById(1);
        log.info(address.toString());
    }

}
