package net.trajano.jee.jaspic;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;

import net.trajano.jee.domain.dao.UserDAO;

/**
 * This is used to provide the server auth module on the application rather than
 * being globally configured in a container. The following is an example of
 * registering the provider in a {@link javax.servlet.ServletContextListener}.
 *
 * <pre>
 *
 * &#064;WebListener
 * public class Initializer implements
 *     ServletContextListener {
 *
 *     &#064;Override
 *     public void contextInitialized(final ServletContextEvent sce) {
 *
 *         Map&lt;String, String&gt; options = new HashMap&lt;&gt;();
 *         options.put(AuthModuleConfigProvider.SERVER_AUTH_MODULE_CLASS, OpenIDConnectAuthModule.class.getName());
 *         AuthConfigFactory.getFactory()
 *             .registerConfigProvider(AuthModuleConfigProvider.class.getName(), options, &quot;HttpServlet&quot;, null, null);
 *     }
 * }
 * </pre>
 */
public class HttpHeaderAuthConfigProvider implements
    AuthConfigProvider {

    private final UserDAO userDAO;

    /**
     * This is called by
     * {@link AuthConfigFactory#registerConfigProvider(String, Map, String, String, String)}
     * when registering the provider.
     *
     * @param userDAO
     *            user DAO
     */
    public HttpHeaderAuthConfigProvider(final UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code null}
     */
    @Override
    public ClientAuthConfig getClientAuthConfig(final String layer,
        final String appContext,
        final CallbackHandler handler) throws AuthException {

        return null;
    }

    @Override
    public ServerAuthConfig getServerAuthConfig(final String layer,
        final String appContext,
        final CallbackHandler handler) throws AuthException {

        return new ServerAuthModuleAuthConfig(layer, appContext, handler, userDAO);
    }

    /**
     * Does nothing.
     */
    @Override
    public void refresh() {

        // Does nothing.
    }

}
