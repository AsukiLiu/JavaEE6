package org.asuki.ldap;

import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.google.common.io.Resources;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;

public abstract class AbstractTestBase {

    private static final int PORT = 34343;
    protected static final String BASE_DN = "dc=example, dc=com";
    private static final String LDIF_NAME = "example.ldif";

    private static InMemoryDirectoryServer server;

    @BeforeClass
    public void before() throws Exception {

        String ldifPath = Resources.getResource(LDIF_NAME).getPath();

        InMemoryListenerConfig listenerConfig = InMemoryListenerConfig
                .createLDAPConfig("test listener", PORT);

        InMemoryDirectoryServerConfig serverConfig = new InMemoryDirectoryServerConfig(
                BASE_DN);
        serverConfig.addAdditionalBindCredentials("cn=admin,dc=example,dc=com",
                "password");
        serverConfig.setListenerConfigs(listenerConfig);

        server = new InMemoryDirectoryServer(serverConfig);
        server.importFromLDIF(true, ldifPath);
        server.startListening();
    }

    @AfterClass
    public void shutdown() {
        if (null != server) {
            server.shutDown(true);
        }
    }
    
    @BeforeMethod
    public void setup() {
        initMocks(this);
    }
    
    @Spy
    protected Logger log = LoggerFactory.getLogger(getClass().getName());
}
