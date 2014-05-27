package org.asuki.web.bean;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.slf4j.Logger;

import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@RequestScoped
public class IndexBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Setter
    @Getter
    private String text;

    public void validate(FacesContext context, UIComponent component,
            Object value) {

        String text = value.toString().trim();

        checkArgument(!isNullOrEmpty(text), "Input must not be empty");

        if (text.equals("error")) {
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