package lv.company.edup.infrastructure.filters;

import lv.company.edup.infrastructure.time.TimePrettifier;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpInboundFilter implements Filter {

    private String[] exclude = new String[]{
            "woff", "woff2", "html", "css", "js", "png", "jpg"
    };

    @Inject Logger logger;

    private AtomicInteger request = new AtomicInteger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        int id = request.incrementAndGet();
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        ResetableHttpServletRequestWrapper wrapper = new ResetableHttpServletRequestWrapper(request);
        String requestBody = getRequestBody(wrapper);

        long start = System.currentTimeMillis();

        ResetableHttpServletResponseWrapper responseWrapper = null;
        if (logger.isDebugEnabled()
                && StringUtils.equals(wrapper.getHeader(HttpHeaders.ACCEPT), MediaType.APPLICATION_JSON)
                && !StringUtils.endsWithAny(request.getRequestURI(), exclude)
                && !StringUtils.contains(request.getRequestURI(), "/edup/api/private/files")) {
            responseWrapper = new ResetableHttpServletResponseWrapper((HttpServletResponse) servletResponse);
        }

        chain.doFilter(wrapper, responseWrapper != null ? responseWrapper : servletResponse);

        buildLogMessage(request, start, id);

        if (logger.isDebugEnabled()) {

            if (StringUtils.isNotBlank(requestBody)) {
                logger.debug("#{} >>> {}", id, requestBody);
            }

            String responseBody = responseWrapper != null ? responseWrapper.getRawData() : null;
            if (StringUtils.isNotBlank(responseBody)) {
                logger.debug("#{} <<< {}", id, responseBody);
            }
        }
    }

    private String getRequestBody(ResetableHttpServletRequestWrapper wrapper) {
        String requestBody = null;
        if (StringUtils.equals(wrapper.getHeader(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON) &&
                (StringUtils.equals(HttpMethod.POST, wrapper.getMethod()) || StringUtils.equals(HttpMethod.PUT, wrapper.getMethod()))) {
            try {
                requestBody = IOUtils.toString(wrapper.getReader());
            } catch (IOException e) {
                logger.warn(e.getLocalizedMessage());
            } finally {
                wrapper.resetInputStream();
            }
        }
        return requestBody;
    }

    @Override
    public void destroy() {
        System.out.println();
    }

    private void buildLogMessage(final HttpServletRequest request, long start, int id) {
        String requestURI = request.getRequestURI();
        if (StringUtils.endsWithAny(requestURI, exclude)) {
            return;
        }

        StrBuilder builder = new StrBuilder();
        String method = request.getMethod();

        builder.append(method).append(" #").append(id);
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            builder.append(" [").append(principal.getName()).append("]");
        }
        builder.append(" [").append(TimePrettifier.pretty(System.currentTimeMillis() - start)).append("]");
        builder.append(": ").append(requestURI);
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            builder.append("?").append(queryString);
        }

        logger.info(builder.toString());
    }
}
