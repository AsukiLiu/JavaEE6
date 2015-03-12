package org.asuki.web.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.asuki.deltaSpike.annotation.Admin;
import org.asuki.deltaSpike.annotation.Employee;

@Named
public class SecurityBean {

    @Inject
    private FacesContext facesContext;

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

}
