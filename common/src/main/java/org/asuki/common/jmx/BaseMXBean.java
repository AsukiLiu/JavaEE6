package org.asuki.common.jmx;

import static com.google.common.base.Strings.repeat;
import static java.lang.String.format;
import static java.lang.System.out;

import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import lombok.SneakyThrows;

public abstract class BaseMXBean {

    private static final String DOMAIN = "Asuki";

    @PostConstruct
    protected void postConstruct() {
        register(getObjectName());
    }

    @PreDestroy
    protected void preDestroy() {
        unregister(getObjectName());
    }

    private void register(ObjectName objectName) {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try {
            mBeanServer.registerMBean(this, getObjectName());
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException
                | NotCompliantMBeanException e) {

            throw new IllegalStateException(e);
        }

    }

    private void unregister(ObjectName objectName) {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try {
            if (mBeanServer.isRegistered(objectName)) {
                mBeanServer.unregisterMBean(objectName);
            }
        } catch (MBeanRegistrationException | InstanceNotFoundException e) {

            throw new IllegalStateException(e);
        }

    }

    protected abstract String getType();

    @SneakyThrows
    private ObjectName getObjectName() {

        return new ObjectName(format("%s:type=%s", DOMAIN, getType()));
    }

    /**
     * List all JVM MBeans
     * 
     * @param args
     */
    public static void main(String[] args) {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectInstance> instances = mBeanServer.queryMBeans(null, null);
        Iterator<ObjectInstance> iterator = instances.iterator();

        while (iterator.hasNext()) {
            ObjectInstance instance = iterator.next();
            out.println("Class Name: " + instance.getClassName());
            out.println("Object Name: " + instance.getObjectName());
            out.println(repeat("-", 100));
        }
    }

}
