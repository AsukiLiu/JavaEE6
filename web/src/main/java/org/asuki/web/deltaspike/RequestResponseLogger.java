package org.asuki.web.deltaspike;

import static java.lang.System.currentTimeMillis;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.deltaspike.core.api.lifecycle.Destroyed;
import org.apache.deltaspike.core.api.lifecycle.Initialized;
import org.slf4j.Logger;

@SessionScoped
public class RequestResponseLogger implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    private Long start;
    private Long duration;

    public void requestStart(@Observes @Initialized HttpServletRequest req) {
        log.info("Request URI: " + req.getRequestURI());
        log.info("Request URL: " + req.getRequestURL().toString());
        start = currentTimeMillis();
    }

    public void responseEnd(@Observes @Destroyed HttpServletResponse resp) {
        duration = currentTimeMillis() - start;
        log.info("Response time: " + duration + "ms");
    }

}
