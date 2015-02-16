package org.asuki.ldap;

import static com.unboundid.ldap.sdk.ResultCode.SUCCESS;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.asuki.ldap.LdapConnectionAdapter.LDAP_POST;
import static org.asuki.ldap.LdapConnectionAdapter.LDAPS_POST;

import java.util.Properties;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LdapConnectionProviderTest extends AbstractTestBase {

    @Test(dataProvider = "propertyData")
    public void shouldConnect(int port, boolean useSSL) {

        Properties props = new Properties();
        props.put("servers", format("127.0.0.1:%d,127.0.0.1:389", port));
        props.put("bindDN", "cn=admin,dc=example,dc=com");
        props.put("bindPassword", "password");
        props.put("useSSL", String.valueOf(useSSL));
        props.put("numConnections", "2");
        props.put("maxWaitTime", "3000");

        LdapConnectionProvider ldapProvider = new LdapConnectionProvider(props);
        assertThat(ldapProvider.getResultCode(), is(SUCCESS));
    }

    @DataProvider
    public Object[][] propertyData() {
        // @formatter:off
        return new Object[][] { 
            { LDAP_POST, false, }, 
            { LDAPS_POST, true }, 
        };
        // @formatter:on
    }
}
