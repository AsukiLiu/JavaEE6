package org.asuki.ldap;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.transform;
import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.hamcrest.Matcher;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.io.Resources;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

public class LdapTest {

    private static final int PORT = 34343;
    private static final String BASE_DN = "dc=example, dc=com";
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

    @Test
    public void shouldSearchBySimplePagedResultsControl() throws LDAPException {

        final LdapSearch ldapSearch = new LdapSearch();

        // @formatter:off
        ResultCode resultCode = ldapSearch.doSearchBySimplePagedResultsControl(
                BASE_DN, 
                SearchScope.SUB,
                Filter.create("(objectClass=inetOrgPerson)"));
        // @formatter:on

        assertThat(resultCode, is(SUCCESS));
    }

    @Test
    public void shouldSearchByVirtualListViewRequestControl()
            throws LDAPException {

        final LdapSearch ldapSearch = new LdapSearch();

        // @formatter:off
        List<SearchResultEntry> entries = ldapSearch.doSearchByVirtualListViewRequestControl(
                BASE_DN, 
                SearchScope.SUB,
                Filter.create("(objectClass=inetOrgPerson)"));
        // @formatter:on

        Function<SearchResultEntry, String> getName = new Function<SearchResultEntry, String>() {
            @Override
            public String apply(SearchResultEntry entry) {
                return on(" ").join(entry.getAttributeValue("sn"),
                        entry.getAttributeValue("cn"));
            }
        };
        List<String> names = transform(entries, getName);

        assertThat(names.toString(),
                is("[osaka taro, tokyo jiro, tokyo saburo, tokyo taro]"));
    }

    @Test(dataProvider = "authzData")
    public void shouldAuthorizate(String password, Matcher<Object> matcher)
            throws LDAPException {

        final LdapAuth ldapAuth = new LdapAuth();

        // @formatter:off
        String authzId = ldapAuth.authorizate(
                "uid=u001,ou=sale,dc=example,dc=com", 
                password);
        // @formatter:on

        assertThat(authzId, is(matcher));
    }

    @DataProvider
    private Object[][] authzData() {
        return new Object[][] {
                { "p-1", is("dn:uid=u001,ou=sale,dc=example,dc=com") },
                { "incorrect", nullValue() }, };
    }

}
