package org.asuki.web.bean;

import static org.picketlink.Identity.AuthenticationResult.FAILED;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.asuki.deltaSpike.annotation.Admin;
import org.asuki.deltaSpike.annotation.Employee;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

@Named
public class SecurityBean {

    @Inject
    private FacesContext facesContext;

    @Inject
    private Identity identity;

    @Employee
    public void executeByEmployee() {
        facesContext.addMessage(null, new FacesMessage(
                "Executed by employee role"));
    }

    @Admin
    public void executeByAdmin() {
        facesContext.addMessage(null,
                new FacesMessage("Executed by admin role"));
    }

    public String getStackTrace() {
        Throwable throwable = (Throwable) FacesContext.getCurrentInstance()
                .getExternalContext().getRequestMap()
                .get("javax.servlet.error.exception");

        StringBuilder builder = new StringBuilder();
        builder.append(throwable.getMessage()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            builder.append(element).append("\n");
        }
        return builder.toString();
    }

    public void login() {
        AuthenticationResult result = identity.login();
        if (FAILED.equals(result)) {
            facesContext.addMessage(null, new FacesMessage(
                    "Authentication was unsuccessful."));
        }
    }
}
