package net.trajano.jee.domain.dao.impl;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@ApplicationScoped
public class JpaProvider {

    private DataSource ds;

    private EntityManager em;

    @Produces
    public DataSource getDataSource() {

        return ds;
    }

    @Produces
    public EntityManager getEntityManager() {

        return em;
    }

    /**
     * Sets/injects the entity manager.
     *
     * @param em
     *            entity manager
     */
    @Resource(lookup = "java:app/ds")
    public void setDataSource(final DataSource ds) {

        this.ds = ds;
    }

    /**
     * Sets/injects the entity manager.
     *
     * @param em
     *            entity manager
     */
    @PersistenceContext
    public void setEntityManager(final EntityManager em) {

        this.em = em;
    }
}
