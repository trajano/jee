package net.trajano.jee.web;

import javax.inject.Inject;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.jaspic.AuthModuleConfigProvider;

@WebListener
public class JaspicInitializer implements
    ServletContextListener {

    private String registrationID;

    @Inject
    private UserDAO userDAO;

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

        AuthConfigFactory.getFactory().removeRegistration(registrationID);
    }

    @Override
    public void contextInitialized(final ServletContextEvent sce) {

        System.out.println("userdao in listener " + userDAO);
        final ServletContext context = sce.getServletContext();
        registrationID = AuthConfigFactory.getFactory()
            .registerConfigProvider(new AuthModuleConfigProvider(userDAO), "HttpServlet",
                context.getVirtualServerName() + " " + context.getContextPath(), "JEE Sample");
    }
}
