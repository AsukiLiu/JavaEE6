package org.asuki.web.bean;

import static java.lang.String.format;

import java.util.concurrent.TimeUnit;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

import org.asuki.web.deltaspike.annotation.ContextPath;
import org.asuki.web.deltaspike.annotation.HeaderParam;
import org.asuki.web.deltaspike.annotation.RequestParam;

@Model
public class RequestBean {

    @Inject
    @RequestParam
    private String delay;

    @Inject
    @HeaderParam("Host")
    private String host;

    @Inject
    @ContextPath
    private String ctxPath;

    @Setter
    @Getter
    private String name;

    public String showInfo() throws InterruptedException {
        TimeUnit.SECONDS.sleep(Long.valueOf(delay));

        String msg = format("Host[%s], Path[%s], Name[%s]", host, ctxPath, name);
        name = null;
        return msg;
    }

}
