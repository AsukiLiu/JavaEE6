package org.asuki.ldap;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;
import static java.lang.System.out;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.asuki.ldap.listener.CustomAsyncSearchResultListener;
import org.asuki.ldap.listener.CustomSearchResultListener;
import org.asuki.ldap.util.ControlPrinter;
import org.hamcrest.Matcher;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.io.Resources;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.AsyncSearchResultListener;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchResultListener;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.controls.PasswordExpiredControl;
import com.unboundid.ldap.sdk.controls.SimplePagedResultsControl;

public class LdapTest {

    private static final int PORT = 34343;
    private static final String BASE_DN = "dc=example, dc=com";
    private static final String LDIF_NAME = "example.ldif";

    private static final String DN_UID_IS_U001 = "uid=u001,ou=sale,dc=example,dc=com";

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

    @BeforeMethod
    public void setup() {
        initMocks(this);
    }

    @Spy
    private Logger log = LoggerFactory.getLogger(getClass().getName());

    @InjectMocks
    private LdapSearch ldapSearch;

    @InjectMocks
    @Spy
    private SearchResultListener searchResultListener = new CustomSearchResultListener();

    @InjectMocks
    @Spy
    private AsyncSearchResultListener asyncSearchResultListener = new CustomAsyncSearchResultListener();

    @Test
    public void shouldSearch() throws LDAPException {

        // @formatter:off
        ResultCode resultCode = ldapSearch.doSearch(
                "ou=develop,dc=example,dc=com", 
                SearchScope.SUB,
                Filter.createPresenceFilter("sn"));
                // Filter.createEqualityFilter("sn", "tokyo"));
        // @formatter:on

        assertThat(resultCode, is(SUCCESS));
    }

    @Test
    public void shouldSearchBySimplePagedResultsControl() throws LDAPException {

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
                DN_UID_IS_U001, 
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

    @Test
    public void shouldChangePassword() {
        final LdapAuth ldapAuth = new LdapAuth();

        // first time
        ResultCode resultCode1 = ldapAuth.changePassword(DN_UID_IS_U001, "p-1",
                "p-new");

        assertThat(resultCode1, is(ResultCode.SUCCESS));

        // second time
        ResultCode resultCode2 = ldapAuth.changePassword(DN_UID_IS_U001, "p-1",
                "p-new2");

        assertThat(resultCode2, is(ResultCode.INVALID_CREDENTIALS));

        // third time
        ResultCode resultCode3 = ldapAuth.changePassword(DN_UID_IS_U001,
                "p-new", "p-other");

        assertThat(resultCode3, is(ResultCode.SUCCESS));
    }

    @Test
    public void shouldPrintControls() {
        @SuppressWarnings("unchecked")
        List<? extends Control> controls = newArrayList(
                new PasswordExpiredControl(), new SimplePagedResultsControl(10));

        for (Control control : controls) {
            ControlPrinter controlPrinter = new ControlPrinter(control);

            out.println(controlPrinter.printControl());
        }
    }

}
