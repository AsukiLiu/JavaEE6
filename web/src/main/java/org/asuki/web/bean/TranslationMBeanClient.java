package org.asuki.web.bean;

import static java.lang.String.format;

import java.lang.management.ManagementFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.asuki.deltaSpike.jmx.TranslationMBean;
import org.slf4j.Logger;

@Named
@ApplicationScoped
public class TranslationMBeanClient {

    @Inject
    private Logger log;

    private static final String OBJECT_NAME = "org.apache.deltaspike:type=MBeans,name=%s";

    private final MBeanServer server = ManagementFactory
            .getPlatformMBeanServer();
    private ObjectName translationMBeanName;

    public TranslationMBeanClient() {
        try {
            translationMBeanName = new ObjectName(format(OBJECT_NAME,
                    TranslationMBean.class.getName()));
        } catch (MalformedObjectNameException e) {
            log.error(null, e);
        }
    }

    public int getTranslations() {
        return (int) invokeOperation("getTranslations");
    }

    public void reset() {
        invokeOperation("reset");
    }

    private Object invokeOperation(String operation) {
        try {
            return server.invoke(translationMBeanName, operation, null, null);
        } catch (Exception e) {
            log.error(null, e);
        }

        return null;
    }

}
