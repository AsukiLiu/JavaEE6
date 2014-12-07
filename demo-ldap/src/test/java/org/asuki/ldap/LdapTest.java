package org.asuki.ldap;

import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.io.Resources;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchScope;

public class LdapTest {

    private static final int PORT = 34343;
    private static final String BASE_DN = "dc=example, dc=com";
    private static final String LDIF_NAME = "example.ldif";

    private InMemoryDirectoryServer server;

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

        InMemoryDirectoryServer server = new InMemoryDirectoryServer(
                serverConfig);
        server.importFromLDIF(true, ldifPath);
        server.startListening();
    }

    @AfterClass
    public void shutdown() {
        if (null != server) {
            server.shutDown(true);
        }
    }

    @Test
    public void shouldSearch() throws LDAPException {

        final LdapSearch ldapSearch = new LdapSearch();

        // @formatter:off
        ResultCode resultCode = ldapSearch.doSearch(
                "ou=develop,dc=example,dc=com", 
                SearchScope.SUB,
                Filter.createEqualityFilter("sn", "tokyo"));
        // @formatter:on

        assertThat(resultCode, is(SUCCESS));
    }

}
