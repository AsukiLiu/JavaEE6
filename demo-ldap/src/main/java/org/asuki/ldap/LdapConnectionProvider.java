package org.asuki.ldap;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.getFirst;
import static com.google.common.collect.Iterables.getLast;
import static java.lang.Integer.parseInt;
import static java.lang.Math.max;

import java.security.GeneralSecurityException;
import java.util.Properties;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.primitives.Ints;
import com.unboundid.ldap.sdk.BindRequest;
import com.unboundid.ldap.sdk.FailoverServerSet;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SimpleBindRequest;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

public class LdapConnectionProvider {

    private static Logger log = LoggerFactory
            .getLogger(LdapConnectionProvider.class);

    private LDAPConnectionPool connectionPool;

    @Getter
    private ResultCode resultCode;

    public LdapConnectionProvider(Properties props) {
        try {
            init(props);
        } catch (LDAPException e) {
            resultCode = e.getResultCode();
            log.error("Failed to create connection pool", e);
        } catch (Exception e) {
            log.error("Failed to create connection pool", e);
        }
    }

    private void init(Properties props) throws NumberFormatException,
            LDAPException, GeneralSecurityException {

        Iterable<String> servers = Splitter.on(",").split(
                props.getProperty("servers"));
        String bindDN = props.getProperty("bindDN");
        String bindPassword = props.getProperty("bindPassword");
        boolean useSSL = Boolean.valueOf(props.getProperty("useSSL"))
                .booleanValue();
        int numConnections = parseInt(props.getProperty("numConnections"));
        String maxWaitTime = props.getProperty("maxWaitTime");

        Multimap<String, Integer> multiMap = ArrayListMultimap.create();
        for (String server : servers) {
            Iterable<String> addressAndPort = Splitter.on(":").split(server);
            multiMap.put(getFirst(addressAndPort, null),
                    parseInt(getLast(addressAndPort)));
        }

        String[] addresses = Iterables.toArray(multiMap.keys(), String.class);
        int[] ports = Ints.toArray(multiMap.values());

        BindRequest bindRequest = createBindRequest(bindDN, bindPassword);
        FailoverServerSet failoverSet = createFailoverServerSet(useSSL,
                addresses, ports);

        connectionPool = new LDAPConnectionPool(failoverSet, bindRequest,
                numConnections);
        if (connectionPool != null) {
            connectionPool.setCreateIfNecessary(true);
            if (!isNullOrEmpty(maxWaitTime)) {
                connectionPool
                        .setMaxWaitTimeMillis(Long.parseLong(maxWaitTime));
            }

            this.resultCode = ResultCode.SUCCESS;
        }
    }

    private BindRequest createBindRequest(String bindDN, String bindPassword) {
        if (isNullOrEmpty(bindDN)) {
            return new SimpleBindRequest();
        }

        return new SimpleBindRequest(bindDN, bindPassword);
    }

    private FailoverServerSet createFailoverServerSet(boolean useSSL,
            String[] addresses, int[] ports) throws GeneralSecurityException {

        LDAPConnectionOptions connectionOptions = new LDAPConnectionOptions();
        connectionOptions.setConnectTimeoutMillis(100 * 1000);
        connectionOptions.setAutoReconnect(true);

        if (useSSL) {
            SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
            return new FailoverServerSet(addresses, ports,
                    sslUtil.createSSLSocketFactory(), connectionOptions);
        }

        return new FailoverServerSet(addresses, ports, connectionOptions);
    }

    public int getSupportedLdapVersion() {
        int result = 0;

        if (connectionPool == null) {
            return result;
        }

        try {
            String[] supportedLDAPVersions = connectionPool.getRootDSE()
                    .getAttributeValues("supportedLDAPVersion");
            if (isEmpty(supportedLDAPVersions)) {
                return result;
            }

            for (String supportedLDAPVersion : supportedLDAPVersions) {
                result = max(result, parseInt(supportedLDAPVersion));
            }
        } catch (Exception e) {
            log.error("Failed to get supportedLDAPVersion", e);
        }

        return result;
    }

    public LDAPConnection getConnection() throws LDAPException {
        return connectionPool.getConnection();
    }

    public void releaseConnection(LDAPConnection connection) {
        connectionPool.releaseConnection(connection);
    }

    public void releaseConnection(LDAPConnection connection, LDAPException e) {
        connectionPool.releaseConnectionAfterException(connection, e);
    }

    public void releaseDefunctConnection(LDAPConnection connection) {
        connectionPool.releaseDefunctConnection(connection);
    }

    public void closeConnectionPool() {
        connectionPool.close();
    }

    public boolean isConnected() {
        if (connectionPool == null) {
            return false;
        }

        boolean isConnected = false;
        try {
            LDAPConnection connection = getConnection();
            isConnected = connection.isConnected();
            releaseConnection(connection);
        } catch (LDAPException e) {
            log.error("Failed to get connection", e);
        }

        return isConnected;
    }

    private static <T> boolean isEmpty(T[] objects) {
        return (objects == null) || (objects.length == 0);
    }

}
