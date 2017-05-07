package net.trajano.jee.jaspic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.trajano.jee.domain.dao.UserDAO;

public class HttpHeaderAuthModule implements
    ServerAuthModule,
    ServerAuthContext {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(HttpHeaderAuthModule.class.getName());

    private static final String WSCREDENTIAL_SECURITYNAME = "com.ibm.wsspi.security.cred.securityName";

    private static final String WSCREDENTIAL_UNIQUEID = "com.ibm.wsspi.security.cred.uniqueId";

    /**
     * Callback handler that is passed in initialize by the container. This
     * processes the callbacks which are objects that populate the "subject".
     */
    private CallbackHandler handler;

    /**
     * Mandatory flag.
     */
    private boolean mandatory;

    /**
     * I am using UserDAO here for illustrative purposes, in reality there
     * should be no user information within this application as that should be
     * managed by a directory.
     */
    private final UserDAO userDAO;

    public HttpHeaderAuthModule(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void cleanSubject(final MessageInfo messageInfo,
        final Subject subject) throws AuthException {

        // Not used
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getSupportedMessageTypes() {

        return new Class<?>[] {
            HttpServletRequest.class,
            HttpServletResponse.class
        };
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void initialize(final MessagePolicy requestPolicy,
        final MessagePolicy responsePolicy,
        final CallbackHandler h,
        final Map options) throws AuthException {

        handler = h;
        mandatory = requestPolicy.isMandatory();
    }

    @Override
    public AuthStatus secureResponse(final MessageInfo messageInfo,
        final Subject serviceSubject) throws AuthException {

        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public AuthStatus validateRequest(final MessageInfo messageInfo,
        final Subject client,
        final Subject serviceSubject) throws AuthException {

        final HttpServletRequest req = (HttpServletRequest) messageInfo.getRequestMessage();
        final HttpServletResponse resp = (HttpServletResponse) messageInfo.getResponseMessage();
        try {
            if (!mandatory && !req.isSecure()) {
                return AuthStatus.SUCCESS;
            }
            if (!req.isSecure()) {
                resp.sendError(HttpURLConnection.HTTP_FORBIDDEN, "SSL Required");
                return AuthStatus.SEND_FAILURE;
            }
            Collections.list(req.getHeaderNames());
            final String username = req.getHeader("X-Forwarded-User");
            if (username == null && mandatory) {
                // return with a not found to hide the fact that a protected resource was requested.
                return AuthStatus.FAILURE;
            } else if (username == null) {
                return AuthStatus.SUCCESS;
            }

            if (!userDAO.isUsernameExist(username)) {
                return AuthStatus.FAILURE;
            }
            if (System.getProperty("was.install.root") != null) {
                websphereWorkaround(client, "websphere", username);
            }

            handler.handle(new Callback[] {
                new CallerPrincipalCallback(client, username),
                new GroupPrincipalCallback(client, new String[] {
                    "users"
                })
            });
            return AuthStatus.SUCCESS;
        } catch (final IOException
            | UnsupportedCallbackException e) {
            LOG.throwing(this.getClass().getName(), "validateRequest", e);
            throw new AuthException(e.getMessage());
        }
    }

    /**
     * Implements the WebSphere workaround. This requires the
     * {@code webspehereUser} to exist in the user registry of WebSphere.
     *
     * @param client
     *            client subject
     * @param websphereUser
     *            existing WebSphere user name.
     * @param principalName
     *            name that is going to be part of the principal.
     * @throws AuthException
     */
    private void websphereWorkaround(final Subject client,
        final String websphereUser,
        final String principalName) throws AuthException {

        try {
            final Object userRegistry = Class.forName("com.ibm.wsspi.security.registry.RegistryHelper").getMethod("getUserRegistry", String.class).invoke(null, new Object[] {
                null
            });
            final String uniqueid = (String) userRegistry.getClass().getMethod("getUniqueUserId", String.class).invoke(userRegistry, websphereUser);

            final Map<String, Object> hashtable = new HashMap<>();
            hashtable.put(WSCREDENTIAL_UNIQUEID, uniqueid);
            hashtable.put(WSCREDENTIAL_SECURITYNAME, principalName);

            client.getPrivateCredentials().add(hashtable);
        } catch (IllegalAccessException
            | InvocationTargetException
            | NoSuchMethodException
            | ClassNotFoundException e) {
            throw new AuthException(e.getMessage());
        }
    }
}
