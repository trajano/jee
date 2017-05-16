package net.trajano.jee.jaspic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
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
import org.mockito.ArgumentMatchers;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.jaspic.HttpHeaderAuthConfigProvider;
import net.trajano.jee.jaspic.HttpHeaderAuthModule;

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
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.isSecure()).thenReturn(true);
        when(messageInfo.getRequestMessage()).thenReturn(httpServletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
        assertEquals(AuthStatus.FAILURE, authContext.validateRequest(messageInfo, null, serviceSubject));
    }

    @Test(expected = AuthException.class)
    public void testFailUnsupportedCallback() throws Exception {

        final UserDAO userDAO = mock(UserDAO.class);
        when(userDAO.isUsernameExist("haruna")).thenReturn(true);
        final CallbackHandler callbackHandler = mock(CallbackHandler.class);
        final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
        final String appContext = "default_host /jee";
        assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
        final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
        assertNotNull(serverAuthConfig);

        assertEquals(appContext, serverAuthConfig.getAppContext());
        final Subject clientSubject = new Subject();
        final Subject serviceSubject = new Subject();
        final HttpHeaderAuthModule authModule = (HttpHeaderAuthModule) serverAuthConfig.getAuthContext("SOMECONTEXTID", serviceSubject, Collections.emptyMap());
        final MessageInfo messageInfo = mock(MessageInfo.class);

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.isSecure()).thenReturn(true);
        when(servletRequest.getHeader("X-Forwarded-User")).thenReturn("haruna");
        when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));

        doThrow(new UnsupportedCallbackException(null)).when(callbackHandler).handle(ArgumentMatchers.any(Callback[].class));
        authModule.validateRequest(messageInfo, clientSubject, serviceSubject);

    }

    @Test(expected = AuthException.class)
    public void testInvalidWebSphere() throws Exception {

        System.setProperty("was.install.root", "nowhere");
        try {
            final UserDAO userDAO = mock(UserDAO.class);
            when(userDAO.isUsernameExist("haruna")).thenReturn(true);
            final CallbackHandler callbackHandler = mock(CallbackHandler.class);
            final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
            final String appContext = "default_host /jee";
            assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
            final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
            assertNotNull(serverAuthConfig);

            assertEquals(appContext, serverAuthConfig.getAppContext());
            final Subject clientSubject = new Subject();
            final Subject serviceSubject = new Subject();
            final MessageInfo messageInfo = mock(MessageInfo.class);

            final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
            when(servletRequest.isSecure()).thenReturn(true);
            when(servletRequest.getHeader("X-Forwarded-User")).thenReturn("haruna");
            when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
            when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
            final Map<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("javax.security.auth.message.MessagePolicy.isMandatory", "true");
            when(messageInfo.getMap()).thenReturn(messageInfoMap);

            final HttpHeaderAuthModule authModule = (HttpHeaderAuthModule) serverAuthConfig.getAuthContext(serverAuthConfig.getAuthContextID(messageInfo), serviceSubject, Collections.emptyMap());
            authModule.getSupportedMessageTypes();
            authModule.validateRequest(messageInfo, clientSubject, serviceSubject);

        } finally {
            System.clearProperty("was.install.root");
        }
    }

    @Test
    public void testNoSsl() throws Exception {

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
        assertEquals(AuthStatus.SEND_CONTINUE, authContext.validateRequest(messageInfo, null, serviceSubject));
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
        final Subject clientSubject = new Subject();
        final Subject serviceSubject = new Subject();
        final MessageInfo messageInfo = mock(MessageInfo.class);

        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.isSecure()).thenReturn(true);
        when(servletRequest.getHeader("X-Forwarded-User")).thenReturn("haruna");
        when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
        final Map<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("javax.security.auth.message.MessagePolicy.isMandatory", "true");
        when(messageInfo.getMap()).thenReturn(messageInfoMap);

        final HttpHeaderAuthModule authModule = (HttpHeaderAuthModule) serverAuthConfig.getAuthContext(serverAuthConfig.getAuthContextID(messageInfo), serviceSubject, Collections.emptyMap());
        authModule.getSupportedMessageTypes();
        final ArgumentCaptor<Callback[]> callbacksCaptor = ArgumentCaptor.forClass(Callback[].class);
        assertEquals(AuthStatus.SUCCESS, authModule.validateRequest(messageInfo, clientSubject, serviceSubject));

        verify(callbackHandler).handle(callbacksCaptor.capture());
        final CallerPrincipalCallback principalCallback = (CallerPrincipalCallback) callbacksCaptor.getValue()[0];
        final GroupPrincipalCallback groupCallback = (GroupPrincipalCallback) callbacksCaptor.getValue()[1];
        assertEquals("haruna", principalCallback.getName());
        assertEquals("users", groupCallback.getGroups()[0]);

        authModule.secureResponse(messageInfo, serviceSubject);
        authModule.cleanSubject(messageInfo, clientSubject);
    }

    @Test
    public void testSuccessNoSecure() throws Exception {

        final UserDAO userDAO = mock(UserDAO.class);
        when(userDAO.isUsernameExist("haruna")).thenReturn(true);
        final CallbackHandler callbackHandler = mock(CallbackHandler.class);
        final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
        final String appContext = "default_host /jee";
        assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
        final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
        assertNotNull(serverAuthConfig);

        assertEquals(appContext, serverAuthConfig.getAppContext());

        final MessageInfo messageInfo = mock(MessageInfo.class);
        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.isSecure()).thenReturn(true);
        when(servletRequest.getHeader("X-Forwarded-User")).thenReturn("haruna");
        when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
        final Map<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("javax.security.auth.message.MessagePolicy.isMandatory", "false");
        when(messageInfo.getMap()).thenReturn(messageInfoMap);

        final Subject clientSubject = new Subject();
        final Subject serviceSubject = new Subject();
        final HttpHeaderAuthModule authModule = (HttpHeaderAuthModule) serverAuthConfig.getAuthContext(serverAuthConfig.getAuthContextID(messageInfo), serviceSubject, Collections.emptyMap());

        authModule.getSupportedMessageTypes();
        final ArgumentCaptor<Callback[]> callbacksCaptor = ArgumentCaptor.forClass(Callback[].class);
        assertEquals(AuthStatus.SUCCESS, authModule.validateRequest(messageInfo, clientSubject, serviceSubject));

        verify(callbackHandler).handle(callbacksCaptor.capture());
        final CallerPrincipalCallback principalCallback = (CallerPrincipalCallback) callbacksCaptor.getValue()[0];
        final GroupPrincipalCallback groupCallback = (GroupPrincipalCallback) callbacksCaptor.getValue()[1];
        assertEquals("haruna", principalCallback.getName());
        assertEquals("users", groupCallback.getGroups()[0]);

        authModule.secureResponse(messageInfo, serviceSubject);
        authModule.cleanSubject(messageInfo, clientSubject);
    }

    @Test
    public void testSuccessNoSecureMissingMapDataNotLoggedIn() throws Exception {

        final UserDAO userDAO = mock(UserDAO.class);
        when(userDAO.isUsernameExist("haruna")).thenReturn(true);
        final CallbackHandler callbackHandler = mock(CallbackHandler.class);
        final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
        final String appContext = "default_host /jee";
        assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
        final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
        assertNotNull(serverAuthConfig);

        assertEquals(appContext, serverAuthConfig.getAppContext());

        final MessageInfo messageInfo = mock(MessageInfo.class);
        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.isSecure()).thenReturn(true);
        when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
        final Map<String, Object> messageInfoMap = new HashMap<>();
        when(messageInfo.getMap()).thenReturn(messageInfoMap);

        final Subject clientSubject = new Subject();
        final Subject serviceSubject = new Subject();
        final HttpHeaderAuthModule authModule = (HttpHeaderAuthModule) serverAuthConfig.getAuthContext(serverAuthConfig.getAuthContextID(messageInfo), serviceSubject, Collections.emptyMap());

        authModule.getSupportedMessageTypes();
        final ArgumentCaptor<Callback[]> callbacksCaptor = ArgumentCaptor.forClass(Callback[].class);
        assertEquals(AuthStatus.SUCCESS, authModule.validateRequest(messageInfo, clientSubject, serviceSubject));

        verify(callbackHandler, never()).handle(callbacksCaptor.capture());
    }

    @Test
    public void testSuccessNoSecureNoSsl() throws Exception {

        final UserDAO userDAO = mock(UserDAO.class);
        when(userDAO.isUsernameExist("haruna")).thenReturn(true);
        final CallbackHandler callbackHandler = mock(CallbackHandler.class);
        final HttpHeaderAuthConfigProvider authConfigProvider = new HttpHeaderAuthConfigProvider(userDAO);
        final String appContext = "default_host /jee";
        assertNull(authConfigProvider.getClientAuthConfig("HttpServlet", appContext, callbackHandler));
        final ServerAuthConfig serverAuthConfig = authConfigProvider.getServerAuthConfig("HttpServlet", appContext, callbackHandler);
        assertNotNull(serverAuthConfig);

        assertEquals(appContext, serverAuthConfig.getAppContext());

        final MessageInfo messageInfo = mock(MessageInfo.class);
        final HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.isSecure()).thenReturn(false);
        when(servletRequest.getHeader("X-Forwarded-User")).thenReturn("haruna");
        when(messageInfo.getRequestMessage()).thenReturn(servletRequest);
        when(messageInfo.getResponseMessage()).thenReturn(mock(HttpServletResponse.class));
        final Map<String, Object> messageInfoMap = new HashMap<>();
        when(messageInfo.getMap()).thenReturn(messageInfoMap);

        final Subject clientSubject = new Subject();
        final Subject serviceSubject = new Subject();
        final HttpHeaderAuthModule authModule = (HttpHeaderAuthModule) serverAuthConfig.getAuthContext(serverAuthConfig.getAuthContextID(messageInfo), serviceSubject, Collections.emptyMap());

        authModule.getSupportedMessageTypes();
        final ArgumentCaptor<Callback[]> callbacksCaptor = ArgumentCaptor.forClass(Callback[].class);
        assertEquals(AuthStatus.SUCCESS, authModule.validateRequest(messageInfo, clientSubject, serviceSubject));

        verify(callbackHandler, never()).handle(callbacksCaptor.capture());
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
