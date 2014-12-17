package org.asuki.ldap.util;

import static com.google.common.base.Joiner.on;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.unboundid.ldap.sdk.Control;

public class ControlPrinter {

    private final Control control;

    public ControlPrinter(Control control) {
        this.control = control;
    }

    public String printControl() {
        final StringBuilder builder = new StringBuilder();

        List<String> infoList = newArrayList(control.getClass()
                .getCanonicalName(), control.getControlName(),
                control.getOID(), "");

        on('\n').appendTo(builder, infoList);
        control.toString(builder);

        return builder.toString();
    }

}
