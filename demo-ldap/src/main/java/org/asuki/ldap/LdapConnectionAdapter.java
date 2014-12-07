package org.asuki.ldap;

import static com.unboundid.util.Debug.debugException;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;

public class LdapConnectionAdapter implements AutoCloseable {

    private LDAPConnection connection;

    public final LDAPConnection getConnection() throws LDAPException {

        try {
            // Local OpenLDAP
            // connection = new LDAPConnection("127.0.0.1", 389);
            // connection.bind("cn=admin,dc=nodomain", "ldap");

            // In memory server
            connection = new LDAPConnection("127.0.0.1", 34343);
            connection.bind("cn=admin,dc=example,dc=com", "password");
        } catch (LDAPException e) {
            debugException(e);
            throw e;
        }

        return connection;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

}
