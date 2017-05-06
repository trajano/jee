package net.trajano.jee.web.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.junit.Test;

import net.trajano.jee.web.JaspicInitializer;

public class JaspicInitializerTest {

    @Test
    public void testInitializer() {

        try {
            AuthConfigFactory.setFactory(mock(AuthConfigFactory.class));
            final ServletContextEvent servletContextEvent = mock(ServletContextEvent.class);
            when(servletContextEvent.getServletContext()).thenReturn(mock(ServletContext.class));
            final JaspicInitializer jaspicInitializer = new JaspicInitializer();
            jaspicInitializer.contextInitialized(servletContextEvent);
            jaspicInitializer.contextDestroyed(servletContextEvent);
        } finally {
            AuthConfigFactory.setFactory(null);
        }
    }
}
