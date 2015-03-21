package org.asuki.service.cluster;

import org.jboss.as.clustering.singleton.SingletonService;
import org.jboss.as.clustering.singleton.election.NamePreference;
import org.jboss.as.clustering.singleton.election.PreferredSingletonElectionPolicy;
import org.jboss.as.clustering.singleton.election.SimpleSingletonElectionPolicy;
import org.jboss.as.server.ServerEnvironment;
import org.jboss.as.server.ServerEnvironmentService;
import org.jboss.msc.service.DelegatingServiceContainer;
import org.jboss.msc.service.ServiceActivator;
import org.jboss.msc.service.ServiceActivatorContext;
import org.jboss.msc.service.ServiceController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HaTimerServiceActivator implements ServiceActivator {

    private static final Logger LOG = LoggerFactory.getLogger(HaTimerServiceActivator.class);

    @Override
    public void activate(ServiceActivatorContext context) {
        LOG.info("HaTimerServiceImpl will be installed");

        // As a clustered singleton service during deployment
        HaTimerServiceImpl service = new HaTimerServiceImpl();
        SingletonService<String> singleton = new SingletonService<String>(
                service, HaTimerServiceImpl.SINGLETON_SERVICE_NAME);

//        singleton.setElectionPolicy(new PreferredSingletonElectionPolicy(
//                new SimpleSingletonElectionPolicy(), new NamePreference(
//                        "nodeA/cluster")));

        singleton
                .build(new DelegatingServiceContainer(context
                        .getServiceTarget(), context.getServiceRegistry()))
                .addDependency(ServerEnvironmentService.SERVICE_NAME,
                        ServerEnvironment.class, service.env)
                .setInitialMode(ServiceController.Mode.ACTIVE).install();
    }
}
