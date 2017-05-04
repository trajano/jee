package net.trajano.jee.web;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.config.AuthConfigFactory;
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

        final Map<String, String> options = new HashMap<>();
        AuthConfigFactory.getFactory()
            .registerConfigProvider(AuthModuleConfigProvider.class.getName(), options, "HttpServlet", null, null);
    }
}
