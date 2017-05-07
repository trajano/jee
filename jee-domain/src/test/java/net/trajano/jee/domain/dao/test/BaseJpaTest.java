package net.trajano.jee.domain.dao.test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Provides basic framework for JPA testing.
 *
 * @author Archimedes Trajano
 */
public abstract class BaseJpaTest {

    protected static EntityManager em;

    private static EntityManagerFactory emf;

    private static ValidatorFactory vf;

    /**
     * Sets up JPA infrastructure.
     */
    @BeforeClass
    public static void setupJpa() {

        vf = Validation.buildDefaultValidatorFactory();
        emf = Persistence.createEntityManagerFactory("test-pu");
        em = emf.createEntityManager();
    }

    /**
     * Teardown JPA infrastructure.
     */
    @AfterClass
    public static void tearDownJpa() {

        em.close();
        emf.close();
        vf.close();
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
