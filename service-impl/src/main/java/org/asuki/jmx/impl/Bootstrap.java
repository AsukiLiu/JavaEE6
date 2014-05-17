package org.asuki.jmx.impl;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.asuki.service.BootstrapService;
import org.asuki.common.jmx.BaseMXBean;
import org.asuki.jmx.BootstrapMXBean;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

@SuppressWarnings("deprecation")
@Singleton
@Startup
public class Bootstrap extends BaseMXBean implements BootstrapMXBean {

    private static final String PERSISTENCE_UNIT = "mysqlU";

    @EJB
    private BootstrapService bootstrapService;

    @Override
    public void cleanDatabase() {

        Ejb3Configuration cfg = new Ejb3Configuration().configure(
                PERSISTENCE_UNIT, null);

        SchemaExport schemaExport = new SchemaExport(
                cfg.getHibernateConfiguration());

        schemaExport.drop(true, true);
        schemaExport.create(true, true);

    }

    @Override
    public void initializeDatabase() {
        bootstrapService.initializeDatabase();
    }

    @Override
    protected String getType() {

        return getClass().getSimpleName();
    }

}
