package org.asuki.ldap;

import static java.lang.System.out;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionOptions;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;

public class LdapReferralTest {

    private static InMemoryDirectoryServer server1;
    private static InMemoryDirectoryServer server2;

    private static LDAPConnection conn1;
    private static LDAPConnection conn2;

    @BeforeClass
    public void init() throws Exception {
        InMemoryDirectoryServerConfig cfg = new InMemoryDirectoryServerConfig(
                "dc=example,dc=com");

        server1 = new InMemoryDirectoryServer(cfg);
        server2 = new InMemoryDirectoryServer(cfg);

        server1.startListening();
        server2.startListening();

        // LDAPConnectionOptions options = new LDAPConnectionOptions();
        // options.setFollowReferrals(true);
        // conn2 = server2.getConnection(options);

        conn1 = server1.getConnection();
        conn2 = server2.getConnection();

        conn1.add(createTopEntry());
        conn1.add(createReferenceEntry());

        conn2.add(createTopEntry());
        conn2.add(createReferrerEntry());
    }

    @AfterClass
    public void tearDown() {
        conn1.close();
        conn2.close();

        server1.shutDown(true);
        server2.shutDown(true);
    }

    @Test(dataProvider = "data")
    public void testReferral(boolean isFollow, ResultCode expect)
            throws Exception {

        LDAPConnectionOptions options = new LDAPConnectionOptions();
        options.setFollowReferrals(isFollow);

        conn2.setConnectionOptions(options);

        ResultCode actual = ResultCode.SUCCESS;
        try {
            Entry entry = conn2.getEntry("ou=Referral Entry,dc=example,dc=com");
            out.println(entry.toLDIFString());
        } catch (LDAPException e) {
            actual = e.getResultCode();
        }

        assertThat(actual, is(expect));
    }

    // @formatter:off

    @DataProvider
    public Object[][] data() {
        return new Object[][] { 
                { true, ResultCode.SUCCESS },
                { false, ResultCode.REFERRAL }, 
        };
    }

    private String[] createTopEntry() {
        return new String[] {
                "dn: dc=example,dc=com",
                "objectClass: top",
                "objectClass: domain",
                "dc: example"};
    }

    private String[] createReferenceEntry() {
        return new String[] {
                "dn: ou=Referral Entry,dc=example,dc=com",
                "objectClass: top",
                "objectClass: organizationalUnit",
                "ou: Referral Entry",
                "description: This is a referral entry"};
    }

    private String[] createReferrerEntry() {
        return new String[] {
                "dn: ou=Referral Entry,dc=example,dc=com",
                "objectClass: top",
                "objectClass: referral",
                "objectClass: extensibleObject",
                "ou: Referral Entry",
                "ref: ldap://localhost:" + server1.getListenPort() +
                     "/ou=Referral Entry,dc=example,dc=com"
                };
    }

    // @formatter:on
}
