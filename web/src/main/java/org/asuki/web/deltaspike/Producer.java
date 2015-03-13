package org.asuki.web.deltaspike;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.asuki.web.deltaspike.annotation.ContextPath;
import org.asuki.web.deltaspike.annotation.HeaderParam;
import org.asuki.web.deltaspike.annotation.RequestParam;

public class Producer {

    @Inject
    @DeltaSpike
    private ServletContext ctx;

    @Inject
    @DeltaSpike
    private HttpServletRequest req;

    @Produces
    @ContextPath
    public String contextPath() {
        return ctx.getContextPath();
    }

    @Produces
    @HeaderParam
    public String headerParam(InjectionPoint ip) {
        HeaderParam headerParam = ip.getAnnotated().getAnnotation(
                HeaderParam.class);
        String paramName = headerParam.value().equals("") ? ip.getMember()
                .getName() : headerParam.value();
        return req.getHeader(paramName);
    }

    @Produces
    @RequestParam
    public String requestParam(InjectionPoint ip) {
        RequestParam requestParam = ip.getAnnotated().getAnnotation(
                RequestParam.class);
        String paramName = requestParam.value().equals("") ? ip.getMember()
                .getName() : requestParam.value();
        return req.getParameter(paramName);
    }

}
