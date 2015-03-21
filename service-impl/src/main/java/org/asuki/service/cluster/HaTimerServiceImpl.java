package org.asuki.service.cluster;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.asuki.service.Scheduler;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initialized only once in a cluster
 */
public class HaTimerServiceImpl implements Service<String> {

    private static final Logger LOG = LoggerFactory
            .getLogger(HaTimerServiceImpl.class);

    public static final ServiceName SINGLETON_SERVICE_NAME = ServiceName.JBOSS
            .append("ha", "singleton", "timer");

    private static final String GLOBAL_JNDI_NAME = "global/demo-app/demo-service-impl/SchedulerImpl!org.asuki.service.Scheduler";

    private final AtomicBoolean started = new AtomicBoolean(false);

    private String nodeName;

    final InjectedValue<ServerEnvironment> env = new InjectedValue<>();

    @Override
    public String getValue() throws IllegalStateException,
            IllegalArgumentException {

        if (!started.get()) {
            throw new IllegalStateException("NOT READY: " + getClassName());
        }
        return this.nodeName;
    }

    @Override
    public void start(StartContext context) throws StartException {

        if (!started.compareAndSet(false, true)) {
            throw new StartException("Already started");
        }

        LOG.info("Start timer service: {}", getClassName());

        this.nodeName = this.env.getValue().getNodeName();

        try {
            this.<Scheduler> lookup().start(
                    "HA Singleton timer @" + this.nodeName + " " + new Date());
        } catch (NamingException e) {
            throw new StartException("Could not start timer", e);
        }
    }

    @Override
    public void stop(StopContext context) {

        if (!started.compareAndSet(true, false)) {
            LOG.warn("NOT ACTIVE: {}", getClassName());
            return;
        }

        LOG.info("Stop timer service: {}", getClassName());

        try {
            this.<Scheduler> lookup().stop();
        } catch (NamingException e) {
            LOG.error("Could not stop timer", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T lookup() throws NamingException {
        InitialContext ic = new InitialContext();
        return (T) ic.lookup(GLOBAL_JNDI_NAME);
    }

    private String getClassName() {
        return getClass().getName();
    }
}
