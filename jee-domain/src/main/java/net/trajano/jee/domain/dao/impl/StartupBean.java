package net.trajano.jee.domain.dao.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Singleton
@Startup
public class StartupBean {

    @Resource(name = "java:app/ds")
    private DataSource ds;

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
