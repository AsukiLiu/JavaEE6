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

import org.asuki.ldap.util.ControlPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.asn1.ASN1OctetString;
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
import com.unboundid.ldap.sdk.SearchResultReference;
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

    private Logger log = LoggerFactory.getLogger(getClass().getName());

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
                final SearchResult searchResult1 = connection.search(baseDn,
                        scope, filter);

                printSearchEntries(searchResult1.getSearchEntries());
                printResponseControl(searchResult1);

                // Approach two
                final LDAPConnectionPool connectionPool = new LDAPConnectionPool(
                        connection, POOL_SIZE);

                final SearchRequest searchRequest2 = new SearchRequest(baseDn,
                        SearchScope.SUB, filter, createAttributes());
                searchRequest2.setSizeLimit(SIZE_LIMIT);
                searchRequest2.setTimeLimitSeconds(TIME_LIMIT);
                final SearchResult searchResult2 = connectionPool
                        .search(searchRequest2);

                printSearchEntries(searchResult2.getSearchEntries());

//                // Approach three
//                final SearchRequest searchRequest3 = new SearchRequest(this,
//                        baseDn, scope, DereferencePolicy.NEVER, 0, 0, false,
//                        filter);
//
//                final SearchResult searchResult3 = connection
//                        .search(searchRequest3);
//
//                log.info("Entries returned:  {}", searchResult3.getEntryCount());
//                log.info("References returned:  {}",
//                        searchResult3.getReferenceCount());

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
