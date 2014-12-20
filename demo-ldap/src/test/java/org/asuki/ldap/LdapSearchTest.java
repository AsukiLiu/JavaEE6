package org.asuki.ldap;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;
import static java.lang.System.out;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.List;

import org.asuki.ldap.listener.CustomAsyncSearchResultListener;
import org.asuki.ldap.listener.CustomSearchResultListener;
import org.asuki.ldap.util.ControlPrinter;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.testng.annotations.Test;

import com.google.common.base.Function;
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

public class LdapSearchTest extends AbstractTestBase {

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
