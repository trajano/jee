package net.trajano.jee.jaspic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
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

@Dependent
public class HttpHeaderAuthModule implements
    ServerAuthModule,
    ServerAuthContext {

    public static final String WSCREDENTIAL_GROUPS = "com.ibm.wsspi.security.cred.groups";

    public static final String WSCREDENTIAL_PASSWORD = "com.ibm.wsspi.security.cred.password";

    public static final String WSCREDENTIAL_SECURITYNAME = "com.ibm.wsspi.security.cred.securityName";

    public static final String WSCREDENTIAL_UNIQUEID = "com.ibm.wsspi.security.cred.uniqueId";

    public static final String WSCREDENTIAL_USERID = "com.ibm.wsspi.security.cred.userId";

    /**
     * Callback handler that is passed in initialize by the container. This
     * processes the callbacks which are objects that populate the "subject".
     */
    private CallbackHandler handler;

    /**
     * Mandatory flag.
     */
    private boolean mandatory;

    @Inject
    private UserDAO usersDAO;

    @Override
    public void cleanSubject(final MessageInfo messageInfo,
        final Subject subject) throws AuthException {

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
        System.out.println("HERE" + usersDAO);
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
        System.out.println("HERE!!!!!");
        try {
            if (!mandatory && !req.isSecure()) {
                return AuthStatus.SUCCESS;
            }
            if (!req.isSecure()) {
                resp.sendError(HttpURLConnection.HTTP_FORBIDDEN, "SSL Required");
                return AuthStatus.SEND_FAILURE;
            }
            final String username = req.getHeader("X-Forwarded-User");
            if (username == null && mandatory) {
                return AuthStatus.FAILURE;
            } else if (username == null && !mandatory) {
                return AuthStatus.SUCCESS;
            }

            try {
                // WebSphere workarounds
                System.out.println("WebSphere Workaround");
                final Method method = Class.forName("com.ibm.wsspi.security.registry.RegistryHelper").getMethod("getUserRegistry", String.class);
                System.out.println(method);
                final Object userRegistry = method.invoke(null, new Object[] {
                    null
                });
                System.out.println("WebSphere Workaround..." + userRegistry);
                final Method method2 = Class.forName("com.ibm.websphere.security.UserRegistry").getMethod("getUniqueUserId", String.class);
                System.out.println(method2);
                final String uniqueid = (String) method2.invoke(userRegistry, "websphere");
                //            final String password = "websphere";

                final Hashtable<String, Object> hashtable = new Hashtable<>();
                hashtable.put(WSCREDENTIAL_UNIQUEID, uniqueid);
                hashtable.put(WSCREDENTIAL_SECURITYNAME, username);
                //          hashtable.put(WSCREDENTIAL_PASSWORD, password);
                hashtable.put(WSCREDENTIAL_GROUPS, new ArrayList(Collections.singletonList("appusers")));

                System.out.println(hashtable);
                client.getPrivateCredentials().add(hashtable);
            } catch (IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException
                | ClassNotFoundException e) {
                e.printStackTrace();
                throw new AuthException(e.getMessage());
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
            e.printStackTrace();
            throw new AuthException(e.getMessage());
        }
    }
}
