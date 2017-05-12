package net.trajano.jee.domain.dao.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import net.trajano.jee.domain.dao.impl.JpaProvider;

/**
 * Provides basic framework for JPA testing.
 *
 * @author Archimedes Trajano
 */
public abstract class BaseJpaTest {

    protected static EntityManager em;

    private static EntityManagerFactory emf;

    private static ValidatorFactory vf;

    private static Weld weld;

    /**
     * Sets up JPA infrastructure.
     *
     * @throws IOException
     * @throws SecurityException
     */
    @BeforeClass
    public static void setupInfrastructure() throws SecurityException,
        IOException {

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("basejpatest-logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        }

        weld = new Weld();
        final WeldContainer container = weld.initialize();
        final JpaProvider jpaProvider = container.select(JpaProvider.class).get();
        vf = Validation.buildDefaultValidatorFactory();
        final Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.provider", "org.eclipse.persistence.jpa.PersistenceProvider");
        props.put("javax.persistence.bean.manager", container.getBeanManager());
        emf = Persistence.createEntityManagerFactory("test-pu", props);
        em = emf.createEntityManager();
        jpaProvider.setEntityManager(em);
    }

    /**
     * Teardown infrastructure.
     */
    @AfterClass
    public static void tearDownInfrastructure() {

        em.close();
        emf.close();
        vf.close();
        weld.shutdown();
        LogManager.getLogManager().reset();
    }

    /**
     * Starts up a transaction before test.
     */
    @Before
    public void beginTransaction() {

        em.getTransaction().begin();
    }

    /**
     * Rolls back the transaction. to prevent saving any data.
     */
    @After
    public void rollbackTransaction() {

        em.getTransaction().rollback();
    }
}
