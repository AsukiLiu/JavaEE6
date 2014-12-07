package org.asuki.ldap;

import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.DereferencePolicy;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
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

                List<SearchResultEntry> entries = searchResult1
                        .getSearchEntries();
                for (SearchResultEntry entry : entries) {
                    log.info(entry.getDN());
                }

                // Approach two
                final SearchRequest searchRequest = new SearchRequest(this,
                        baseDn, scope, DereferencePolicy.NEVER, 0, 0, false,
                        filter);

                final SearchResult searchResult2 = connection
                        .search(searchRequest);

                log.info("Entries returned:  {}", searchResult2.getEntryCount());
                log.info("References returned:  {}",
                        searchResult2.getReferenceCount());

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

}
