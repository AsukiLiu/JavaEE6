package org.asuki.ldap;

import static com.google.common.io.Resources.getResource;
import static com.unboundid.ldap.listener.InMemoryListenerConfig.createLDAPConfig;
import static com.unboundid.ldap.listener.InMemoryListenerConfig.createLDAPSConfig;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.asuki.ldap.LdapConnectionAdapter.HOST;
import static org.asuki.ldap.LdapConnectionAdapter.LDAP_POST;
import static org.asuki.ldap.LdapConnectionAdapter.LDAPS_POST;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.InMemoryRequestHandler;
import com.unboundid.ldap.listener.InMemorySASLBindHandler;
import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.Control;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.ssl.KeyStoreKeyManager;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

public abstract class AbstractTestBase {

    protected static final String BASE_DN = "dc=example, dc=com";
    private static final String LDIF_NAME = "example.ldif";

    private static final String SERVER_KEY_STORE_FILE = getResource(
            "keystore.p12").getPath();
    private static final char[] SERVER_KEY_STORE_PIN = "password".toCharArray();
    private static final String KEY_STORE_FORMAT = "PKCS12";
    private static final String CERTIFICATE_ALIAS = "server-cert";

    private static InMemoryDirectoryServer server;

    @BeforeClass
    public void before() throws Exception {

        String ldifPath = getResource(LDIF_NAME).getPath();

        InMemoryDirectoryServerConfig serverConfig = new InMemoryDirectoryServerConfig(
                BASE_DN);
        serverConfig.addAdditionalBindCredentials("cn=admin,dc=example,dc=com",
                "password");
        serverConfig.setListenerConfigs(createConfigForLdap(),
                createConfigForLdaps());

        serverConfig.addSASLBindHandler(new InMemorySASLBindHandler() {

            @Override
            public String getSASLMechanismName() {
                // http://en.wikipedia.org/wiki/Simple_Authentication_and_Security_Layer
                return "EXTERNAL";
            }

            @Override
            public BindResult processSASLBind(InMemoryRequestHandler handler,
                    int msgId, DN dn, ASN1OctetString credentials,
                    List<Control> controls) {
                return new BindResult(new LDAPResult(1, ResultCode.SUCCESS));
            }

        });

        server = new InMemoryDirectoryServer(serverConfig);
        server.importFromLDIF(true, ldifPath);
        server.startListening();
    }

    private InMemoryListenerConfig createConfigForLdap() throws LDAPException,
            UnknownHostException, GeneralSecurityException {

        SSLUtil serverSSLUtil = new SSLUtil(new KeyStoreKeyManager(
                SERVER_KEY_STORE_FILE, SERVER_KEY_STORE_PIN, KEY_STORE_FORMAT,
                CERTIFICATE_ALIAS), null);

        // LDAP (with StartTLS)
        return createLDAPConfig("LDAP listener", InetAddress.getByName(HOST),
                LDAP_POST, serverSSLUtil.createSSLSocketFactory());
        // createLDAPConfig("LDAP listener", LDAP_POST);
    }

    private InMemoryListenerConfig createConfigForLdaps() throws LDAPException,
            UnknownHostException, GeneralSecurityException {

        SSLUtil serverSSLUtil = new SSLUtil(new KeyStoreKeyManager(
                SERVER_KEY_STORE_FILE, SERVER_KEY_STORE_PIN, KEY_STORE_FORMAT,
                CERTIFICATE_ALIAS), null);

        SSLUtil clientSSLUtil = new SSLUtil(new TrustAllTrustManager());

        // LDAP over SSL
        return createLDAPSConfig("LDAPS listener", InetAddress.getByName(HOST),
                LDAPS_POST, serverSSLUtil.createSSLServerSocketFactory(),
                clientSSLUtil.createSSLSocketFactory());

    }

    @AfterClass
    public void shutdown() {
        if (null != server) {
            server.shutDown(true);
        }
    }

    @BeforeMethod
    public void setup() {
        initMocks(this);
    }

    @Spy
    protected Logger log = LoggerFactory.getLogger(getClass().getName());
}
