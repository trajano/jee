package net.trajano.jee.web;

import javax.inject.Inject;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.jaspic.HttpHeaderAuthConfigProvider;

@WebListener
public class JaspicInitializer implements
    ServletContextListener {

    /**
     * Registration ID for the provider. Used for removing the registration when
     * {@link #contextDestroyed(ServletContextEvent)}.
     */
    private String registrationID;

    @Inject
    private UserDAO userDAO;

    /**
     * Removes the registration for the AuthConfigProvider. {@inheritDoc}
     */
    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

        AuthConfigFactory.getFactory().removeRegistration(registrationID);
    }

    /**
     * Registers the custom AuthConfigProvider. {@inheritDoc}
     */
    @Override
    public void contextInitialized(final ServletContextEvent sce) {

        final ServletContext context = sce.getServletContext();
        registrationID = AuthConfigFactory.getFactory()
            .registerConfigProvider(new HttpHeaderAuthConfigProvider(userDAO), "HttpServlet",
                context.getVirtualServerName() + " " + context.getContextPath(), "JEE Sample");
    }
}
