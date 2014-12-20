package org.asuki.ldap;

import static com.google.common.collect.Lists.newArrayList;
import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;
import static com.unboundid.ldap.sdk.ResultCode.UNWILLING_TO_PERFORM;
import static com.unboundid.ldap.sdk.controls.SimplePagedResultsControl.PAGED_RESULTS_OID;
import static com.unboundid.ldap.sdk.controls.VirtualListViewRequestControl.VIRTUAL_LIST_VIEW_REQUEST_OID;
import static com.unboundid.ldap.sdk.controls.VirtualListViewResponseControl.VIRTUAL_LIST_VIEW_RESPONSE_OID;
import static org.asuki.ldap.util.SupportedFeature.isControlSupported;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.asuki.ldap.listener.CustomSearchResultListener;
import org.asuki.ldap.util.ControlPrinter;
import org.slf4j.Logger;

import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.AsyncRequestID;
import com.unboundid.ldap.sdk.AsyncSearchResultListener;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.DereferencePolicy;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchResultListener;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.ldap.sdk.controls.ServerSideSortRequestControl;
import com.unboundid.ldap.sdk.controls.SimplePagedResultsControl;
import com.unboundid.ldap.sdk.controls.SortKey;
import com.unboundid.ldap.sdk.controls.VirtualListViewRequestControl;
import com.unboundid.ldap.sdk.controls.VirtualListViewResponseControl;
import com.unboundid.util.LDAPTestUtils;

public final class LdapSearch {

    private static final int POOL_SIZE = 5;
    private static final int PAGE_SIZE = 2;

    private static final int BEFORE_COUNT = 0;
    private static final int AFTER_COUNT = 1;
    private static final int OFFSET = 2;

    private static final int SIZE_LIMIT = 10;
    private static final int TIME_LIMIT = 3;

    @Inject
    private Logger log;

    @Inject
    @New(CustomSearchResultListener.class)
    private SearchResultListener searchResultListener;

    @Inject
    private AsyncSearchResultListener asyncSearchResultListener;

    public ResultCode doSearch(String baseDn, SearchScope scope, Filter filter) {

        ResultCode resultCode = SUCCESS;

        try (LdapConnectionAdapter adapter = new LdapConnectionAdapter()) {

            final LDAPConnection connection;
            try {
                connection = adapter.getConnection();

                log.info("Connected to {}:{}",
                        connection.getConnectedAddress(),
                        connection.getConnectedPort());

            } catch (LDAPException e) {
                log.error("Error connecting to the directory server", e);
                return e.getResultCode();
            }

            try {
                log.info("baseDn:{}, scope:{}, filter:{}", baseDn, scope,
                        filter);

                // Approach one
                log.info("=========searchByLDAPConnection=========");
                searchByLDAPConnection(baseDn, scope, filter, connection);

                // Approach two
                log.info("=========searchByLDAPConnectionPool=========");
                searchByLDAPConnectionPool(baseDn, scope, filter, connection);

                // Approach three
                log.info("=========searchBySearchResultListener=========");
                searchBySearchResultListener(baseDn, scope, filter, connection);

                // Approach four
                log.info("=========searchByAsyncSearchResultListener=========");
                searchByAsyncSearchResultListener(baseDn, scope, filter,
                        connection);

            } catch (LDAPException e) {
                log.error("Error occurred while processing the search", e);

                log.error("Result Code:  {}({})", e.getResultCode().intValue(),
                        e.getResultCode().getName());

                if (e.getMatchedDN() != null) {
                    log.error("Matched DN:  {}", e.getMatchedDN());
                }

                if (e.getReferralURLs() != null) {
                    log.error("Referral URLs:  {}",
                            Arrays.toString(e.getReferralURLs()));
                }

                if (resultCode == SUCCESS) {
                    resultCode = e.getResultCode();
                }
            }

        } catch (Exception e) {
            log.error("Error occurred", e);
            resultCode = ResultCode.OTHER;
        }

        return resultCode;
    }

    private void searchByLDAPConnection(String baseDn, SearchScope scope,
            Filter filter, LDAPConnection connection) throws LDAPException {

        SearchResult searchResult = connection.search(baseDn, scope, filter);

        printSearchEntries(searchResult.getSearchEntries());
        printResponseControl(searchResult);
    }

    private void searchByLDAPConnectionPool(String baseDn, SearchScope scope,
            Filter filter, LDAPConnection connection) throws LDAPException {

        LDAPConnectionPool connectionPool = new LDAPConnectionPool(connection,
                POOL_SIZE);

        SearchRequest searchRequest = new SearchRequest(baseDn,
                SearchScope.SUB, filter, createAttributes());
        searchRequest.setSizeLimit(SIZE_LIMIT);
        searchRequest.setTimeLimitSeconds(TIME_LIMIT);

        SearchResult searchResult = connectionPool.search(searchRequest);

        printSearchEntries(searchResult.getSearchEntries());
    }

    private void searchBySearchResultListener(String baseDn, SearchScope scope,
            Filter filter, LDAPConnection connection) throws LDAPException {

        SearchRequest searchRequest = new SearchRequest(searchResultListener,
                baseDn, scope, DereferencePolicy.NEVER, SIZE_LIMIT, TIME_LIMIT,
                false, filter);

        SearchResult searchResult = connection.search(searchRequest);

        log.info("Entries returned:  {}", searchResult.getEntryCount());
        log.info("References returned:  {}", searchResult.getReferenceCount());
    }

    private void searchByAsyncSearchResultListener(String baseDn,
            SearchScope scope, Filter filter, LDAPConnection connection)
            throws LDAPException, InterruptedException {

        SearchRequest searchRequest = new SearchRequest(
                asyncSearchResultListener, baseDn, scope,
                DereferencePolicy.NEVER, SIZE_LIMIT, TIME_LIMIT, false, filter);

        AsyncRequestID asyncRequestID = connection.asyncSearch(searchRequest);

        SearchResult searchResult = (SearchResult) asyncRequestID.get();

        log.info("Entries returned:  {}", searchResult.getEntryCount());
        log.info("References returned:  {}", searchResult.getReferenceCount());
    }

    private String[] createAttributes() {
        List<String> requestedAttributes = newArrayList("cn", "sn");
        String[] attributes = new String[requestedAttributes.size()];
        requestedAttributes.toArray(attributes);
        return attributes;
    }

    public ResultCode doSearchBySimplePagedResultsControl(String baseDn,
            SearchScope scope, Filter filter) {

        ResultCode resultCode = SUCCESS;

        try (LdapConnectionAdapter adapter = new LdapConnectionAdapter()) {

            final LDAPConnection connection;
            try {
                connection = adapter.getConnection();

                log.info("Connected to {}:{}",
                        connection.getConnectedAddress(),
                        connection.getConnectedPort());

            } catch (LDAPException e) {
                log.error("Error connecting to the directory server", e);
                return e.getResultCode();
            }

            String controlOID = PAGED_RESULTS_OID;
            if (!isControlSupported(connection, controlOID)) {
                return UNWILLING_TO_PERFORM;
            }

            int numSearches = 0;
            int totalEntriesReturned = 0;

            SearchRequest searchRequest = new SearchRequest(baseDn, scope,
                    filter);
            ASN1OctetString resumeCookie = null;

            while (true) {
                searchRequest.setControls(new SimplePagedResultsControl(
                        PAGE_SIZE, resumeCookie));

                SearchResult searchResult = connection.search(searchRequest);

                numSearches++;
                totalEntriesReturned += searchResult.getEntryCount();
                printSearchEntries(searchResult.getSearchEntries());

                LDAPTestUtils.assertHasControl(searchResult, PAGED_RESULTS_OID);

                SimplePagedResultsControl responseControl = SimplePagedResultsControl
                        .get(searchResult);
                if (responseControl.moreResultsToReturn()) {
                    resumeCookie = responseControl.getCookie();
                } else {
                    break;
                }
            }

            log.info("numSearches:{}, totalEntriesReturned:{}", numSearches,
                    totalEntriesReturned);

        } catch (Exception e) {
            log.error("Error occurred", e);
            resultCode = ResultCode.OTHER;
        }

        return resultCode;
    }

    public List<SearchResultEntry> doSearchByVirtualListViewRequestControl(
            String baseDn, SearchScope scope, Filter filter) {

        List<SearchResultEntry> searchResultEntries = newArrayList();

        try (LdapConnectionAdapter adapter = new LdapConnectionAdapter()) {

            final LDAPConnection connection;
            try {
                connection = adapter.getConnection();

                log.info("Connected to {}:{}",
                        connection.getConnectedAddress(),
                        connection.getConnectedPort());

            } catch (LDAPException e) {
                log.error("Error connecting to the directory server", e);
                return searchResultEntries;
            }

            String controlOID = VIRTUAL_LIST_VIEW_REQUEST_OID;
            if (!isControlSupported(connection, controlOID)) {
                return searchResultEntries;
            }

            int numSearches = 0;
            int totalEntriesReturned = 0;

            SearchRequest searchRequest = new SearchRequest(baseDn, scope,
                    filter);
            int vlvOffset = 1;
            int vlvContentCount = 0;
            ASN1OctetString vlvContextID = null;

            while (true) {
                searchRequest.setControls(new ServerSideSortRequestControl(
                        new SortKey("sn"), new SortKey("cn")),
                        new VirtualListViewRequestControl(vlvOffset,
                                BEFORE_COUNT, AFTER_COUNT, vlvContentCount,
                                vlvContextID));

                SearchResult searchResult = connection.search(searchRequest);

                numSearches++;
                totalEntriesReturned += searchResult.getEntryCount();
                printSearchEntries(searchResult.getSearchEntries());
                searchResultEntries.addAll(searchResult.getSearchEntries());

                LDAPTestUtils.assertHasControl(searchResult,
                        VIRTUAL_LIST_VIEW_RESPONSE_OID);

                VirtualListViewResponseControl vlvResponseControl = VirtualListViewResponseControl
                        .get(searchResult);

                vlvContentCount = vlvResponseControl.getContentCount();
                vlvOffset += OFFSET;
                vlvContextID = vlvResponseControl.getContextID();
                if (vlvOffset > vlvContentCount) {
                    break;
                }
            }

            log.info("numSearches:{}, totalEntriesReturned:{}", numSearches,
                    totalEntriesReturned);

        } catch (Exception e) {
            log.error("Error occurred", e);
        }

        return searchResultEntries;
    }

    private void printSearchEntries(Iterable<SearchResultEntry> searchEntries) {
        for (SearchResultEntry entry : searchEntries) {
            log.info(entry.getDN());
            for (Attribute attribute : entry.getAttributes()) {
                log.info("\t{}", attribute.getName());
                log.info("\t\t{}", Arrays.toString(attribute.getValues()));
            }
        }
    }

    private void printResponseControl(SearchResult searchResult) {
        if (!searchResult.hasResponseControl()) {
            return;
        }

        for (Control control : searchResult.getResponseControls()) {
            ControlPrinter controlPrinter = new ControlPrinter(control);

            log.info(controlPrinter.printControl());
        }
    }

}
