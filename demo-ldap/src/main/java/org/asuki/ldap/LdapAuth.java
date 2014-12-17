package org.asuki.ldap;

import static com.unboundid.ldap.sdk.extensions.WhoAmIExtendedRequest.WHO_AM_I_REQUEST_OID;
import static org.asuki.ldap.util.SupportedFeature.isExtendedOperationSupported;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.ldap.sdk.controls.AuthorizationIdentityRequestControl;
import com.unboundid.ldap.sdk.controls.AuthorizationIdentityResponseControl;
import com.unboundid.ldap.sdk.extensions.WhoAmIExtendedRequest;
import com.unboundid.ldap.sdk.extensions.WhoAmIExtendedResult;

public final class LdapAuth {

    private Logger log = LoggerFactory.getLogger(getClass().getName());

    private static final int INITIAL_CONNECTIONS = 2;
    private static final int MAX_CONNECTIONS = 6;
    private static final long RESPONSE_TIMEOUT = 3000;

    public String authorizate(String dn, String password) {

        String authzId = null;

        try (LdapConnectionAdapter adapter = new LdapConnectionAdapter()) {

            final LDAPConnection connection;
            try {
                connection = adapter.getConnection();

                log.info("Who am I: {}", getWhoAmIExtension(connection));
            } catch (LDAPException e) {
                log.error("Error connecting to the directory server", e);
                return null;
            }

            final LDAPConnectionPool connectionPool = adapter
                    .getConnectionPool(connection, INITIAL_CONNECTIONS,
                            MAX_CONNECTIONS);

            BindRequest bindRequest = new SimpleBindRequest(dn, password,
                    new AuthorizationIdentityRequestControl());
            bindRequest.setResponseTimeoutMillis(RESPONSE_TIMEOUT);

            BindResult bindResult = connectionPool.bind(bindRequest);
            AuthorizationIdentityResponseControl authzIdentityResponse = AuthorizationIdentityResponseControl
                    .get(bindResult);
            if (authzIdentityResponse != null) {
                authzId = authzIdentityResponse.getAuthorizationID();
            }
        } catch (Exception e) {
            log.error("Error occurred", e);
        }

        return authzId;
    }

    private String getWhoAmIExtension(LDAPConnection connection)
            throws LDAPException {

        if (!isExtendedOperationSupported(connection, WHO_AM_I_REQUEST_OID)) {
            return "";
        }

        WhoAmIExtendedResult whoAmIExtendedResult = (WhoAmIExtendedResult) connection
                .processExtendedOperation(new WhoAmIExtendedRequest());
        return whoAmIExtendedResult.getAuthorizationID();
    }
}
