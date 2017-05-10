package net.trajano.jee.ws.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.HttpURLConnection;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import net.trajano.jee.ws.ValidateRequestFilter;

public class ValidateRequestFilterTest {

    @Test
    public void testGetOnEndpoint() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("GET");
        when(req.getRequestURI()).thenReturn("/jee-ws/ParticipantCodeFirstService");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.doFilter(req, resp, filterChain);

        verify(filterChain, never()).doFilter(req, resp);
        verify(resp).sendError(HttpURLConnection.HTTP_FORBIDDEN);

    }

    @Test
    public void testHeadIndex() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("HEAD");
        when(req.getRequestURI()).thenReturn("/jee-ws/");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.init(mock(FilterConfig.class));
        filter.doFilter(req, resp, filterChain);

        verify(filterChain).doFilter(req, resp);
        filter.destroy();

    }

    @Test
    public void testIndex() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("GET");
        when(req.getRequestURI()).thenReturn("/jee-ws/");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.init(mock(FilterConfig.class));
        filter.doFilter(req, resp, filterChain);

        verify(filterChain).doFilter(req, resp);
        filter.destroy();

    }

    @Test
    public void testNonSecureRequest() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(false);
        when(req.getMethod()).thenReturn("GET");
        when(req.getQueryString()).thenReturn("wsdl");
        when(req.getRequestURI()).thenReturn("/jee-ws/ParticipantCodeFirstService");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.doFilter(req, resp, filterChain);

        verify(filterChain, never()).doFilter(req, resp);
        verify(resp).sendError(HttpURLConnection.HTTP_FORBIDDEN, "HTTPS Required");

    }

    @Test
    public void testOptionIndex() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("OPTION");
        when(req.getRequestURI()).thenReturn("/jee-ws/");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.init(mock(FilterConfig.class));
        filter.doFilter(req, resp, filterChain);

        verify(filterChain, never()).doFilter(req, resp);
        verify(resp).sendError(HttpURLConnection.HTTP_FORBIDDEN);
        filter.destroy();

    }

    @Test
    public void testPostOnNonEndpoint() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("POST");
        when(req.getRequestURI()).thenReturn("/jee-ws/NotAnEndpoint");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.doFilter(req, resp, filterChain);

        verify(filterChain, never()).doFilter(req, resp);
        verify(resp).sendError(HttpURLConnection.HTTP_FORBIDDEN);

    }

    @Test
    public void testValidServiceRequest1() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("POST");
        when(req.getRequestURI()).thenReturn("/jee-ws/ParticipantManagerService");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.doFilter(req, resp, filterChain);

        verify(filterChain).doFilter(req, resp);

    }

    @Test
    public void testValidServiceRequest2() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("POST");
        when(req.getRequestURI()).thenReturn("/jee-ws/ParticipantCodeFirstService");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.doFilter(req, resp, filterChain);

        verify(filterChain).doFilter(req, resp);

    }

    @Test
    public void testValidWsdlRequest() throws Exception {

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getContextPath()).thenReturn("/jee-ws");
        when(req.isSecure()).thenReturn(true);
        when(req.getMethod()).thenReturn("GET");
        when(req.getQueryString()).thenReturn("wsdl");
        when(req.getRequestURI()).thenReturn("/jee-ws/ParticipantCodeFirstService");

        final HttpServletResponse resp = mock(HttpServletResponse.class);
        final FilterChain filterChain = mock(FilterChain.class);

        final ValidateRequestFilter filter = new ValidateRequestFilter();
        filter.doFilter(req, resp, filterChain);

        verify(filterChain).doFilter(req, resp);

    }
}
