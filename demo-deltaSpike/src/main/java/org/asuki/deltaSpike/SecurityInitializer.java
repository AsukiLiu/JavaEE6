package org.asuki.deltaSpike;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Digest;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import static org.picketlink.idm.model.basic.BasicModel.*;

@Singleton
@Startup
public class SecurityInitializer {

    public static final String EMPLOYEE_ROLE = "employee";
    public static final String ADMIN_ROLE = "admin";

    @Inject
    private PartitionManager partitionManager;

    @PostConstruct
    public void create() {

        IdentityManager identityManager = partitionManager
                .createIdentityManager();

        /*--------testing role---------*/

        User employee = new User("employee");
        employee.setEmail("employee@example.com");
        identityManager.add(employee);
        identityManager.updateCredential(employee, new Password("1234"));

        User admin = new User("admin");
        admin.setEmail("admin@example.com");
        identityManager.add(admin);
        identityManager.updateCredential(admin, new Password("1234"));

        Role employeeRole = new Role(EMPLOYEE_ROLE);
        identityManager.add(employeeRole);
        Role adminRole = new Role(ADMIN_ROLE);
        identityManager.add(adminRole);

        RelationshipManager relationshipManager = partitionManager
                .createRelationshipManager();

        grantRole(relationshipManager, employee, employeeRole);
        grantRole(relationshipManager, admin, adminRole);

        /*--------testing digest---------*/

        User user = new User("digest");
        user.setEmail("digest@example.com");
        identityManager.add(user);

        Digest digestCredential = new Digest();
        digestCredential
            .setRealm("Test Realm")
            .setUsername(user.getLoginName())
            .setPassword("1234");

        identityManager.updateCredential(user, digestCredential);
    }
}
