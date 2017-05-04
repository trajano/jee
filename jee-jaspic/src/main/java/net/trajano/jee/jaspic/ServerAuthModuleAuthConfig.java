package net.trajano.jee.jaspic;

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.MessagePolicy.TargetPolicy;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;

import net.trajano.jee.domain.dao.UserDAO;

public class ServerAuthModuleAuthConfig implements
    ServerAuthConfig {

    /**
     * <p>
     * The {@link MessageInfo} map must contain this key and its associated
     * value, if and only if authentication is required to perform the resource
     * access corresponding to the HttpServletRequest to which the
     * ServerAuthContext will be applied. Authentication is required if use of
     * the HTTP method of the HttpServletRequest at the resource identified by
     * the HttpServletRequest is covered by a Servlet authconstraint, or in a
     * JSR 115 compatible runtime, if the corresponding WebResourcePermission is
     * NOT granted to an unauthenticated caller. In a JSR 115 compatible
     * runtime, the corresponding WebResourcePermission may be constructed
     * directly from the HttpServletRequest as follows:
     * </p>
     *
     * <pre>
     *
     * public WebResourcePermission(HttpServletRequest request);
     * </pre>
     * <p>
     * The authentication context configuration system must use the value of
     * this property to establish the corresponding value within the
     * requestPolicy passed to the authentication modules of the
     * {@link javax.security.auth.message.config.ServerAuthContext} acquired to
     * process the {@link MessageInfo}.
     * </p>
     */
    private static final String JAVAX_SECURITY_AUTH_MESSAGE_MESSAGE_POLICY_IS_MANDATORY = "javax.security.auth.message.MessagePolicy.isMandatory";

    /**
     * Mandatory message policy.
     */
    private static final MessagePolicy MANDATORY = new MessagePolicy(new TargetPolicy[0], true);

    /**
     * Non-mandatory message policy.
     */
    private static final MessagePolicy NON_MANDATORY = new MessagePolicy(new TargetPolicy[0], false);

    private final String appContext;

    private final CallbackHandler handler;

    private final String layer;

    private final UserDAO userDAO;

    public ServerAuthModuleAuthConfig(
        final String layer,
        final String appContext,
        final CallbackHandler handler,
        final UserDAO userDAO) {
        this.appContext = appContext;
        this.layer = layer;
        this.handler = handler;
        this.userDAO = userDAO;
    }

    @Override
    public String getAppContext() {

        return appContext;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public ServerAuthContext getAuthContext(final String authContextID,
        final Subject serviceSubject,
        final Map properties) throws AuthException {

        final ServerAuthContext context = new HttpHeaderAuthModule(userDAO);

        final ServerAuthModule module = (ServerAuthModule) context;
        if (authContextID == null) {
            module.initialize(NON_MANDATORY, NON_MANDATORY, handler, properties);
        } else {
            module.initialize(MANDATORY, MANDATORY, handler, properties);
        }
        return context;
    }

    @Override
    public String getAuthContextID(final MessageInfo messageInfo) {

        final Object isMandatory = messageInfo.getMap()
            .get(JAVAX_SECURITY_AUTH_MESSAGE_MESSAGE_POLICY_IS_MANDATORY);
        if (isMandatory != null && isMandatory instanceof String && Boolean.valueOf((String) isMandatory)) {
            return messageInfo.toString();
        }
        return null;
    }

    @Override
    public String getMessageLayer() {

        return layer;
    }

    @Override
    public boolean isProtected() {

        return true;
    }

    @Override
    public void refresh() {

        // does nothing

    }

}
