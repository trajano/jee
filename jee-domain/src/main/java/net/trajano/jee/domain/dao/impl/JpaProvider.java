package net.trajano.jee.domain.dao.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class JpaProvider {

    private EntityManager em;

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
    @PersistenceContext
    public void setEntityManager(final EntityManager em) {

        this.em = em;
    }
}
