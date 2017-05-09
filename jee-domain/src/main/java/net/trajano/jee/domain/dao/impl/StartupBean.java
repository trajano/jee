package net.trajano.jee.domain.dao.impl;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
@ApplicationScoped
public class StartupBean {

    @PersistenceContext
    private EntityManager em;

    /**
     * Ensures that the database works by performing a operation against it.
     */
    @PostConstruct
    public void ensureDatabase() {

        em.createNamedQuery(NamedQueries.PARTICIPANT_GET_ALL).getResultList();
    }
}
