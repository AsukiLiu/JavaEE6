package org.asuki.web.servlet;

import static org.jgroups.Event.GET_PHYSICAL_ADDRESS;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.asuki.service.AccessService;
import org.infinispan.manager.CacheContainer;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.msc.service.ServiceName;

import org.jgroups.Channel;
import org.jgroups.Event;
import org.jgroups.PhysicalAddress;
import org.jgroups.stack.IpAddress;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.management.*;

@WebServlet("/cluster")
public class ClusterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Resource(lookup = "java:jboss/infinispan/container/singleton")
    private CacheContainer container;

    @Inject
    private AccessService accessService;

    public ClusterServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        getClusterMembersByJGroups();

        getClusterMembersByInfinispan();

        getClusterMembersByJmx();
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        log.info(accessService.getNodeName());
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