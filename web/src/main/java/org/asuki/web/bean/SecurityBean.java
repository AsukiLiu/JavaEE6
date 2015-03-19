package org.asuki.web.bean;

import static org.picketlink.Identity.AuthenticationResult.FAILED;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.asuki.deltaSpike.annotation.Admin;
import org.asuki.deltaSpike.annotation.Employee;
import org.asuki.webservice.ReCaptchaService;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

@Model
public class SecurityBean {

    @Inject
    private FacesContext facesContext;

    @Inject
    private Identity identity;

    @Inject
    private ReCaptchaService reCaptchaService;

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
        if (checkCaptcha()) {
            AuthenticationResult result = identity.login();

            if (FAILED.equals(result)) {
                // if (!identity.isLoggedIn()) {
                facesContext.addMessage(null, new FacesMessage(
                        "Authentication failed."));
            }
        } else {
            facesContext.addMessage(null, new FacesMessage(
                    "Verification failed."));
        }
    }

    private boolean checkCaptcha() {
        return reCaptchaService.verify(getReCaptchaChallenge(),
                getReCaptchaResponse());
    }

    private String getReCaptchaChallenge() {
        return facesContext.getExternalContext().getRequestParameterMap()
                .get("recaptcha_challenge_field");
    }

    private String getReCaptchaResponse() {
        return facesContext.getExternalContext().getRequestParameterMap()
                .get("recaptcha_response_field");
    }

    public String logout() {
        identity.logout();
        return "/security2.xhtml";
    }
}
