package net.trajano.jee.web;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.trajano.jee.jaspic.AuthModuleConfigProvider;

@WebListener
public class JaspicInitializer implements
    ServletContextListener {

    private String registrationID;

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

        AuthConfigFactory.getFactory().removeRegistration(registrationID);
        System.out.println("deregistered " + registrationID);
    }

    @Override
    public void contextInitialized(final ServletContextEvent sce) {

        final ServletContext context = sce.getServletContext();
        registrationID = AuthConfigFactory.getFactory()
            .registerConfigProvider(new AuthModuleConfigProvider(), "HttpServlet",
                context.getVirtualServerName() + " " + context.getContextPath(), "JEE Sample");
        System.out.println("registered " + registrationID);
    }
}
