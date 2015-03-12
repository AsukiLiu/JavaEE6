package org.asuki.deltaSpike;

import org.apache.deltaspike.security.api.authorization.Secures;
import org.asuki.deltaSpike.annotation.Admin;
import org.asuki.deltaSpike.annotation.Employee;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;

import javax.enterprise.context.ApplicationScoped;

import static org.asuki.deltaSpike.SecurityInitializer.ADMIN_ROLE;
import static org.asuki.deltaSpike.SecurityInitializer.EMPLOYEE_ROLE;
import static org.picketlink.idm.model.basic.BasicModel.*;

@ApplicationScoped
public class CustomAuthorizer {

    @Secures
    @Admin
    public boolean isAdmin(Identity identity, IdentityManager identityManager,
            RelationshipManager relationshipManager) throws Exception {

        return hasRole(relationshipManager, identity.getAccount(),
                getRole(identityManager, ADMIN_ROLE));
    }

    @Secures
    @Employee
    public boolean isEmployeeOrAdmin(Identity identity,
            IdentityManager identityManager,
            RelationshipManager relationshipManager) throws Exception {

        return hasRole(relationshipManager, identity.getAccount(),
                getRole(identityManager, EMPLOYEE_ROLE))
                || hasRole(relationshipManager, identity.getAccount(),
                        getRole(identityManager, ADMIN_ROLE));
    }

}
