package org.asuki.ldap.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchResultListener;
import com.unboundid.ldap.sdk.SearchResultReference;

public class CustomSearchResultListener implements SearchResultListener {

    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(getClass().getName());

    @Override
    public void searchEntryReturned(SearchResultEntry searchEntry) {
        log.info(searchEntry.toLDIFString());
    }

    @Override
    public void searchReferenceReturned(SearchResultReference searchReference) {
        log.info(searchReference.toString());
    }

}
