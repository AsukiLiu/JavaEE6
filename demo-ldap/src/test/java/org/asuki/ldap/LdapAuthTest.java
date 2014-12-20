package org.asuki.ldap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.Matcher;
import org.mockito.InjectMocks;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.ResultCode;

public class LdapAuthTest extends AbstractTestBase {

    private static final String DN_UID_IS_U001 = "uid=u001,ou=sale,dc=example,dc=com";

    @InjectMocks
    private LdapAuth ldapAuth;

    @Test(dataProvider = "authzData")
    public void shouldAuthorizate(String password, Matcher<Object> matcher)
            throws LDAPException {

        // @formatter:off
        String authzId = ldapAuth.authorizate(
                DN_UID_IS_U001, 
                password);
        // @formatter:on

        assertThat(authzId, is(matcher));
    }

    @DataProvider
    private Object[][] authzData() {
        return new Object[][] {
                { "p-1", is("dn:uid=u001,ou=sale,dc=example,dc=com") },
                { "incorrect", nullValue() }, };
    }

    @Test
    public void shouldChangePassword() {

        // first time
        ResultCode resultCode1 = ldapAuth.changePassword(DN_UID_IS_U001, "p-1",
                "p-new");

        assertThat(resultCode1, is(ResultCode.SUCCESS));

        // second time
        ResultCode resultCode2 = ldapAuth.changePassword(DN_UID_IS_U001, "p-1",
                "p-new2");

        assertThat(resultCode2, is(ResultCode.INVALID_CREDENTIALS));

        // third time
        ResultCode resultCode3 = ldapAuth.changePassword(DN_UID_IS_U001,
                "p-new", "p-other");

        assertThat(resultCode3, is(ResultCode.SUCCESS));
    }
}
