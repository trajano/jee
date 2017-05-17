package net.trajano.jee.ws;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * This provides a workaround for WebSphere which does not perform an automatic
 * redirect to SSL for anonymous users. It also blocks GET requests to the SOAP
 * endpoints and disables all other methods aside from POST.
 *
 * @author Archimedes Trajano
 */
@WebFilter(urlPatterns = "/*")
public class ValidateRequestFilter implements
    Filter {

    private static final String GET = "GET";

    private static final String HEAD = "HEAD";

    private static final String POST = "POST";

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void destroy() {

        // does nothing.

    }

    @Override
    public void doFilter(final ServletRequest request,
        final ServletResponse response,
        final FilterChain chain) throws IOException,
        ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;
        if (!req.isSecure()) {
            resp.sendError(HttpURLConnection.HTTP_FORBIDDEN, "HTTPS Required");
        } else if (POST.equals(req.getMethod()) && isEndpointRequestURI(req)) {
            chain.doFilter(request, response);
        } else if ((GET.equals(req.getMethod()) || HEAD.equals(req.getMethod())) && !isEndpointRequestURI(req)) {
            chain.doFilter(request, response);
        } else {
            resp.sendError(HttpURLConnection.HTTP_FORBIDDEN);
        }
    }

    /**
     * Does nothing. {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

        // Does nothing
    }

    /**
     * Determines if the request is to an SOAP endpoint directly and has no
     * query string.
     *
     * @param req
     *            request
     * @return true if it is a SOAP endpoint
     */
    private boolean isEndpointRequestURI(@NotNull final HttpServletRequest req) {

        if (req.getQueryString() != null) {
            return false;
        }
        final String path = req.getRequestURI().substring(req.getContextPath().length());
        switch (path) {
        case "/LobManagerService":
        case "/ParticipantManagerService":
        case "/ParticipantCodeFirstService":
            return true;
        default:
            return false;
        }
    }

}
