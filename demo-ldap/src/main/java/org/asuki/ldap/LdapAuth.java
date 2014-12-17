package org.asuki.ldap;

import static com.unboundid.ldap.sdk.controls.PasswordExpiredControl.PASSWORD_EXPIRED_OID;
import static com.unboundid.ldap.sdk.controls.PasswordExpiringControl.PASSWORD_EXPIRING_OID;
import static com.unboundid.ldap.sdk.extensions.WhoAmIExtendedRequest.WHO_AM_I_REQUEST_OID;
import static org.asuki.ldap.util.SupportedFeature.isControlSupported;
import static org.asuki.ldap.util.SupportedFeature.isExtendedOperationSupported;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.ldap.sdk.controls.AuthorizationIdentityRequestControl;
import com.unboundid.ldap.sdk.controls.AuthorizationIdentityResponseControl;
import com.unboundid.ldap.sdk.controls.PasswordExpiredControl;
import com.unboundid.ldap.sdk.controls.PasswordExpiringControl;
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
                log.info("Who am I before bind: {}",
                        getWhoAmIExtension(connection));

                connection.bind(new SimpleBindRequest(dn, password));
                log.info("Who am I after bind: {}",
                        getWhoAmIExtension(connection));
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

            processPasswordControl(connectionPool.getConnection(), bindResult);

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

    private void processPasswordControl(LDAPConnection connection,
            LDAPResult ldapResult) throws LDAPException {

        if (isControlSupported(connection, PASSWORD_EXPIRED_OID)) {
            final PasswordExpiredControl passwordExpiredControl = PasswordExpiredControl
                    .get(ldapResult);

            if (passwordExpiredControl != null) {
                log.warn("Must change password");
            }
        }

        if (isControlSupported(connection, PASSWORD_EXPIRING_OID)) {
            final PasswordExpiringControl passwordExpiringControl = PasswordExpiringControl
                    .get(ldapResult);

            if (passwordExpiringControl != null) {
                log.warn("Password will expire in {} seconds",
                        passwordExpiringControl.getSecondsUntilExpiration());
            }
        }

    }

}
