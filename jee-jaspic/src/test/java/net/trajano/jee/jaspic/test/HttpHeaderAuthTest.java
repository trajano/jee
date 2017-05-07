package net.trajano.jee.jaspic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.jaspic.HttpHeaderAuthConfigProvider;

public class HttpHeaderAuthTest {

    @Test
    public void testFailedAuthentication() throws Exception {

        final UserDAO userDAO = mock(UserDAO.class);
        final CallbackHandler callbackHandler = mock(CallbackHandler.class);
        final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
        final String appContext = "default_host /jee";
        assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
        final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
        assertNotNull(serverAuthConfig);
        assertEquals(appContext, serverAuthConfig.getAppContext());
        final Subject serviceSubject = new Subject();
        final ServerAuthContext authContext = serverAuthConfig.getAuthContext("SOMECONTEXTID", serviceSubject, Collections.emptyMap());
        final MessageInfo messageInfo = mock(MessageInfo.class);
        when(messageInfo.getRequestMessage()).thenReturn(mock(HttpServletRequest.class));
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
        assertEquals(AuthStatus.SEND_FAILURE, authContext.validateRequest(messageInfo, null, serviceSubject));
    }

    @Test
    public void testSuccess() throws Exception {

        final UserDAO userDAO = mock(UserDAO.class);
        when(userDAO.isUsernameExist("haruna")).thenReturn(true);
        final CallbackHandler callbackHandler = mock(CallbackHandler.class);
        final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
        final String appContext = "default_host /jee";
        assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
        final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
        assertNotNull(serverAuthConfig);
        assertEquals(appContext, serverAuthConfig.getAppContext());
        final Subject serviceSubject = new Subject();
        final ServerAuthContext authContext = serverAuthConfig.getAuthContext("SOMECONTEXTID", serviceSubject, Collections.emptyMap());
        final MessageInfo messageInfo = mock(MessageInfo.class);

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.isSecure()).thenReturn(true);
        when(servletRequest.getHeader("X-Forwarded-User")).thenReturn("haruna");
        when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));

        final ArgumentCaptor<Callback[]> callbacksCaptor = ArgumentCaptor.forClass(Callback[].class);
        assertEquals(AuthStatus.SUCCESS, authContext.validateRequest(messageInfo, null, serviceSubject));

        verify(callbackHandler).handle(callbacksCaptor.capture());
        final CallerPrincipalCallback principalCallback = (CallerPrincipalCallback) callbacksCaptor.getValue()[0];
        final GroupPrincipalCallback groupCallback = (GroupPrincipalCallback) callbacksCaptor.getValue()[1];
        assertEquals("haruna", principalCallback.getName());
        assertEquals("users", groupCallback.getGroups()[0]);
    }

    @Test
    public void testUserNotExist() throws Exception {

        final UserDAO userDAO = mock(UserDAO.class);
        final CallbackHandler callbackHandler = mock(CallbackHandler.class);
        final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
        final String appContext = "default_host /jee";
        assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
        final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
        assertNotNull(serverAuthConfig);
        assertEquals(appContext, serverAuthConfig.getAppContext());
        final Subject serviceSubject = new Subject();
        final ServerAuthContext authContext = serverAuthConfig.getAuthContext("SOMECONTEXTID", serviceSubject, Collections.emptyMap());
        final MessageInfo messageInfo = mock(MessageInfo.class);

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.isSecure()).thenReturn(true);
        when(servletRequest.getHeader("X-Forwarded-User")).thenReturn("haruna");
        when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
        assertEquals(AuthStatus.FAILURE, authContext.validateRequest(messageInfo, null, serviceSubject));
    }
}
