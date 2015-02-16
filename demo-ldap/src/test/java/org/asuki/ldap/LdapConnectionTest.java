package org.asuki.ldap;

import static com.google.common.io.Resources.getResource;
import static java.lang.System.out;
import static org.asuki.ldap.LdapConnectionAdapter.HOST;
import static org.asuki.ldap.LdapConnectionAdapter.LDAPS_POST;
import static org.asuki.ldap.LdapConnectionAdapter.LDAP_POST;

import javax.net.ssl.SSLContext;

import org.testng.annotations.Test;

import com.unboundid.ldap.sdk.ExtendedRequest;
import com.unboundid.ldap.sdk.ExtendedResult;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.RootDSE;
import com.unboundid.ldap.sdk.extensions.StartTLSExtendedRequest;
import com.unboundid.util.LDAPTestUtils;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import com.unboundid.util.ssl.TrustStoreTrustManager;

public class LdapConnectionTest extends AbstractTestBase {

    private static final String CLIENT_TRUST_STORE_PATH = getResource(
            "keystore.jks").getPath();

    @Test
    public void shouldConnectLdapOverSSL() throws Exception {
        SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());

        // Establish a secure connection
        LDAPConnection connection = new LDAPConnection(
                sslUtil.createSSLSocketFactory());
        connection.connect(HOST, LDAPS_POST);
        // LDAPConnection(sslUtil.createSSLSocketFactory(), HOST, LDAPS_POST);

        RootDSE rootDSE = connection.getRootDSE();
        out.println(rootDSE);

        connection.close();
    }

    @Test
    public void shouldConnectLdapWithStartTLS() throws Exception {
        // Establish a non-secure connection
        LDAPConnection connection = new LDAPConnection(HOST, LDAP_POST);

        SSLUtil sslUtil = new SSLUtil(new TrustStoreTrustManager(
                CLIENT_TRUST_STORE_PATH));
        SSLContext sslContext = sslUtil.createSSLContext();

        // Secure the connection
        ExtendedRequest startTLSRequest = new StartTLSExtendedRequest(
                sslContext);

        ExtendedResult startTLSResult;
        try {
            startTLSResult = connection
                    .processExtendedOperation(startTLSRequest);
        } catch (LDAPException e) {
            startTLSResult = new ExtendedResult(e);
        }

        LDAPTestUtils
                .assertResultCodeEquals(startTLSResult, ResultCode.SUCCESS);

        RootDSE rootDSE = connection.getRootDSE();
        out.println(rootDSE);

        connection.close();
    }
}
