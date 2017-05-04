package net.trajano.jee.web;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.trajano.jee.jaspic.AuthModuleConfigProvider;

@WebListener
public class JaspicInitializer implements
    ServletContextListener {

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(final ServletContextEvent sce) {

        // Programmatic installation of JASPIC per standards does not work at all with WebSphere.  It must be installed using WebSphere administrative tools.
        if (System.getProperty("was.install.root") != null) {
            return;
        }
        final Map<String, String> options = new HashMap<>();
        final ServletContext context = sce.getServletContext();
        AuthConfigFactory.getFactory()
            .registerConfigProvider(AuthModuleConfigProvider.class.getName(), options, "HttpServlet", sce.getServletContext().getVirtualServerName() + " " + context.getContextPath(), "JEE Sample");
    }
}
