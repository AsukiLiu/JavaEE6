package org.asuki.service.impl;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.asuki.service.AccessService;
import org.asuki.service.cluster.HaTimerServiceImpl;
import org.jboss.as.server.CurrentServiceContainer;
import org.jboss.msc.service.ServiceController;

@Stateless(name = "AccessService")
@Remote(AccessService.class)
public class AccessServiceImpl implements AccessService {

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
}
