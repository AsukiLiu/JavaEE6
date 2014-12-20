package org.asuki.ldap.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.AsyncRequestID;
import com.unboundid.ldap.sdk.AsyncSearchResultListener;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchResultReference;

public class CustomAsyncSearchResultListener implements
        AsyncSearchResultListener {

    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void searchEntryReturned(SearchResultEntry searchEntry) {
        log.info(searchEntry.toLDIFString());
    }

    @Override
    public void searchReferenceReturned(SearchResultReference searchReference) {
        throw new UnsupportedOperationException(
                "searchReferenceReturned not yet supported");
    }

    @Override
    public void searchResultReceived(AsyncRequestID requestID,
            SearchResult searchResult) {
        log.info("requestID:{}, searchResult:{}", requestID.toString(),
                searchResult.toString());
    }

}
