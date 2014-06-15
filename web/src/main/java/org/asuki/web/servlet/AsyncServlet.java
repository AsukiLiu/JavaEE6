package org.asuki.web.servlet;

import static org.asuki.common.Constants.Webs.DEFAULT_CHARSET;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(name = "AsyncServlet", urlPatterns = { "/async" }, loadOnStartup = 1, asyncSupported = true, initParams = {
        @WebInitParam(name = "name", value = "Andy"),
        @WebInitParam(name = "age", value = "20") })
public class AsyncServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding(DEFAULT_CHARSET);
        response.setContentType("text/html;charset=utf-8");

        AsyncContext actx = request.startAsync();
        actx.setTimeout(30 * 1000);
        actx.start(new JobThread(actx));

        log.info("Do another thing ...");

        PrintWriter out = response.getWriter();
        ServletConfig config = getServletConfig();

        out.println(config.getInitParameter("name"));
        out.println(config.getInitParameter("age"));

        // out.flush();
    }

}

class JobThread implements Runnable {

    private Logger log = LoggerFactory.getLogger(getClass());

    private AsyncContext actx;

    public JobThread(AsyncContext actx) {
        this.actx = actx;
    }

    @SneakyThrows
    @Override
    public void run() {

        log.info("Do one thing ...");

        TimeUnit.SECONDS.sleep(2);

        actx.dispatch("/index.xhtml");
    }
}
