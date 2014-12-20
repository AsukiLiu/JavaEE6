package org.asuki.ldap.util;

import static com.unboundid.util.Validator.ensureNotNullWithMessage;

import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPInterface;
import com.unboundid.ldap.sdk.schema.Schema;

public final class SupportedFeature {

    private SupportedFeature() {
    }

    public static boolean isControlSupported(LDAPInterface conn,
            String controlOID) {
        ensureNotNullWithMessage(conn, "conn was null.");
        ensureNotNullWithMessage(controlOID, "controlOID was null.");

        try {
            return conn.getRootDSE().supportsControl(controlOID);
        } catch (LDAPException e) {
            return false;
        }
    }

    public static boolean isExtendedOperationSupported(LDAPInterface conn,
            String extensionOID) {
        ensureNotNullWithMessage(conn, "conn was null.");
        ensureNotNullWithMessage(extensionOID, "extensionOID was null.");

        try {
            return conn.getRootDSE().supportsExtendedOperation(extensionOID);
        } catch (LDAPException e) {
            return false;
        }
    }

    public static boolean isAttributeSupported(LDAPConnection conn,
            String attributeName) {
        ensureNotNullWithMessage(conn, "conn was null.");
        ensureNotNullWithMessage(attributeName, "attributeName was null.");

        Schema schema;
        try {
            schema = Schema.getSchema(conn);
        } catch (LDAPException e) {
            return false;
        }

        return schema.getAttributeType(attributeName) != null;
    }

}
