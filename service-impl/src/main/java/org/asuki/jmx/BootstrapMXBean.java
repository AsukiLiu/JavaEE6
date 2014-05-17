package org.asuki.jmx;

import javax.management.MXBean;

@MXBean
public interface BootstrapMXBean {

    void cleanDatabase();

    void initializeDatabase();
}
