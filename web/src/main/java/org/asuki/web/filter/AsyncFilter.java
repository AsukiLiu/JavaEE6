package org.asuki.web.filter;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Iterables.contains;
import static java.lang.String.format;
import static org.asuki.common.Constants.Webs.DEFAULT_CHARSET;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.common.base.Splitter;

import lombok.NoArgsConstructor;

@WebFilter(filterName = "AsyncFilter", urlPatterns = { "/*" }, asyncSupported = true, initParams = {
        @WebInitParam(name = "name", value = "Andy"),
        @WebInitParam(name = "blackList", value = "Mike,John,Tom") })
@NoArgsConstructor
public class AsyncFilter implements Filter {

    @Inject
    private Logger log;

    private String blackList;
    private String charset;

    public void init(FilterConfig fConfig) throws ServletException {

        blackList = fConfig.getInitParameter("blackList");

        charset = fConfig.getInitParameter("charset");
        if (isNullOrEmpty(charset)) {
            charset = DEFAULT_CHARSET;
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        request.setCharacterEncoding(charset);

        // ★ユーザ認証
        String userName = ((HttpServletRequest) request)
                .getParameter("userName");

        if (!isNullOrEmpty(userName)) {
            Iterable<String> parts = Splitter.on(",").omitEmptyStrings()
                    .trimResults().split(blackList);

            if (!contains(parts, userName)) {
                throw new ServletException(format(
                        "Can not be authenticated [%s]", userName));
            }
        }

        // ★キャッシュ禁止
        ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache");
        ((HttpServletResponse) response).setHeader("Pragma", "no-cache");
        ((HttpServletResponse) response).setDateHeader("Expires", -1);

        chain.doFilter(request, response);
    }

    public void destroy() {
    }

}