package net.trajano.jee.domain.dao.test;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.junit.Test;

/**
 * Ensures that the persistence framework will work with Hibernate.
 *
 * @author Archimedes Trajano
 */
public class HibernateSanityTest {

    @Test
    public void testHibernateTransaction() {

        final ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        final Map<String, String> props = new HashMap<>();
        props.put("javax.persistence.provider", "org.hibernate.jpa.HibernatePersistenceProvider");
        final EntityManagerFactory emf = Persistence.createEntityManagerFactory("test-pu", props);//
        final EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.getTransaction().rollback();
        em.close();
        emf.close();
        vf.close();
    }
}
