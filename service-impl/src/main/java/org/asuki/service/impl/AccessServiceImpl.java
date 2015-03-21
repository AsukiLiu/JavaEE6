package org.asuki.service.impl;

import static org.jgroups.Event.GET_PHYSICAL_ADDRESS;

import java.lang.management.ManagementFactory;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.management.ObjectName;

import org.asuki.service.AccessService;
import org.asuki.service.cluster.HaTimerServiceImpl;
import org.infinispan.manager.CacheContainer;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jgroups.Channel;
import org.jgroups.Event;
import org.jgroups.PhysicalAddress;
import org.jgroups.stack.IpAddress;
import org.slf4j.Logger;

@Stateless(name = "AccessService")
@Remote(AccessService.class)
public class AccessServiceImpl implements AccessService {

    @Inject
    private Logger log;

    @Resource(lookup = "java:jboss/infinispan/container/singleton")
    private CacheContainer container;

    @Override
    public String getNodeName() {

        ServiceController<?> service = CurrentServiceContainer
                .getServiceContainer().getService(
                        HaTimerServiceImpl.SINGLETON_SERVICE_NAME);
        if (service == null) {
            throw new IllegalStateException("NOT FOUND: "
                    + HaTimerServiceImpl.SINGLETON_SERVICE_NAME);
        }

        return (String) service.getValue();
    }

    @Override
    public void getClusterMembers() {

        getClusterMembersByJGroups();

        getClusterMembersByInfinispan();

        getClusterMembersByJmx();
    }

    private void getClusterMembersByJGroups() {

        Channel channel = (Channel) CurrentServiceContainer
                .getServiceContainer()
                .getService(
                        ServiceName.JBOSS.append("jgroups", "channel",
                                "singleton")).getValue();

        List<org.jgroups.Address> members = channel.getView().getMembers();

        for (org.jgroups.Address member : members) {
            PhysicalAddress physicalAddr = (PhysicalAddress) channel
                    .down(new Event(GET_PHYSICAL_ADDRESS, member));
            IpAddress ipAddr = (IpAddress) physicalAddr;
            log.info("IP:{}", ipAddr.getIpAddress().getHostAddress());
        }
    }

    private void getClusterMembersByInfinispan() {

        List<org.infinispan.remoting.transport.Address> members = container
                .getCache().getCacheManager().getMembers();

        for (org.infinispan.remoting.transport.Address member : members) {
            log.info("Member:{}", member.toString());
        }
    }

    private void getClusterMembersByJmx() {
        Object obj = null;

        try {
            obj = ManagementFactory
                    .getPlatformMBeanServer()
                    .getAttribute(
                            ObjectName
                                    .getInstance("jgroups:type=channel,cluster=\"singleton\""),
                            "View");
        } catch (Exception e) {
            log.error("Can not get instance by JMX", e);
        }

        log.info(obj.toString());
    }

}
