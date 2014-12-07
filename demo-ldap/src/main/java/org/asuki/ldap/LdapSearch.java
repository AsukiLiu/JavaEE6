package org.asuki.ldap;

import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.Attribute;
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

public final class LdapSearch implements SearchResultListener {

    private static final long serialVersionUID = 1L;

    private static final int POOL_SIZE = 5;

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
                // Approach one
                final SearchResult searchResult1 = connection.search(baseDn,
                        scope, filter);

                printSearchEntries(searchResult1.getSearchEntries());

                // Approach two
                final LDAPConnectionPool connectionPool = new LDAPConnectionPool(
                        connection, POOL_SIZE);

                final SearchRequest searchRequest2 = new SearchRequest(baseDn,
                        SearchScope.SUB, filter);
                final SearchResult searchResult2 = connectionPool
                        .search(searchRequest2);

                printSearchEntries(searchResult2.getSearchEntries());

                // Approach three
                final SearchRequest searchRequest3 = new SearchRequest(this,
                        baseDn, scope, DereferencePolicy.NEVER, 0, 0, false,
                        filter);

                final SearchResult searchResult3 = connection
                        .search(searchRequest3);

                log.info("Entries returned:  {}", searchResult3.getEntryCount());
                log.info("References returned:  {}",
                        searchResult3.getReferenceCount());

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
        }

        return resultCode;
    }

    @Override
    public void searchEntryReturned(SearchResultEntry entry) {
        log.info(entry.toLDIFString());
    }

    @Override
    public void searchReferenceReturned(SearchResultReference reference) {
        log.info(reference.toString());
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

}
