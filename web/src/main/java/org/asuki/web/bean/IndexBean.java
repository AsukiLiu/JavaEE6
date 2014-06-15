package org.asuki.web.bean;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import org.asuki.common.annotation.TimeLog;
import org.slf4j.Logger;

import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
@TimeLog
public class IndexBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Setter
    @Getter
    private String text;

    @PostConstruct
    private void init() {
        log.info("init");
    }

    @PreDestroy
    private void destroy() {
        log.info("destroy");
    }

    public void validate(FacesContext context, UIComponent component,
            Object value) {

        String input = value.toString().trim();

        checkArgument(!isNullOrEmpty(input), "Input must not be empty");

        if (input.equals("error")) {
            throw new ValidatorException(new FacesMessage("Error"));
        }
    }

    public void valueChanged(ValueChangeEvent event) {

    }

    public void actionListener(ActionEvent e) {

    }

    public String action() {

        return "success";
    }

}