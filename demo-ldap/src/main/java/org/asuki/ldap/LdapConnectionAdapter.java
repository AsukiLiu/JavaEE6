package org.asuki.ldap;

import static com.unboundid.util.Validator.ensureNotNull;
import static com.unboundid.util.Validator.ensureTrue;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;

public class LdapConnectionAdapter implements AutoCloseable {

    private static final int NUM_THREADS = 5;

    private LDAPConnection connection;
    private LDAPConnectionPool connectionPool;

    public final LDAPConnection getConnection() throws LDAPException {

        // Local OpenLDAP
        // connection = new LDAPConnection("127.0.0.1", 389);
        // connection.bind("cn=admin,dc=nodomain", "ldap");

        // In memory server
        connection = new LDAPConnection("127.0.0.1", 34343);
        connection.bind("cn=admin,dc=example,dc=com", "password");

        return connection;
    }

    public final LDAPConnectionPool getConnectionPool(
            LDAPConnection connection, int initialConnections,
            int maxConnections) throws LDAPException {

        ensureNotNull(connection);
        ensureTrue(initialConnections > 0);
        ensureTrue(maxConnections >= initialConnections);

        connectionPool = new LDAPConnectionPool(connection, initialConnections,
                maxConnections);

        return connectionPool;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }

        if (connectionPool != null) {
            connectionPool.close(true, NUM_THREADS);
        }
    }

}
