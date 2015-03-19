package org.asuki.deltaSpike;

import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

import javax.enterprise.event.Observes;

public class HttpSecurityConfiguration {

    public void onInit(@Observes SecurityConfigurationEvent event) {

        SecurityConfigurationBuilder builder = event.getBuilder();

        // @formatter:off

//        builder
//            .http()
//                .allPaths()
//                    .authenticateWith()
//                        //.basic()
//                        .digest()
//                            .realmName("Test Realm");

        // @formatter:on
    }

}
