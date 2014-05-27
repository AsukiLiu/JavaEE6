package org.asuki.web.bean;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class SuccessBean implements Serializable {

    private static final long serialVersionUID = 1L;

    public void actionListener() {

    }

    public String getValueG() {

        return "dummy";
    }

    public void setValueS(String valueS) {

    }

    public void action() {

    }

    public String getResult() {

        return (new Date()).toString();
    }

}
