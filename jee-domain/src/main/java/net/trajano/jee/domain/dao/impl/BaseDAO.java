package net.trajano.jee.domain.dao.impl;

import java.security.Principal;

import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.trajano.jee.domain.entity.BaseEntity;

/**
 * Provides common methods and data to DAOs.
 *
 * @author Archimedes Trajano
 */
public class BaseDAO {

    /**
     * Injected entity manager. Made protected to allow the whole gamut of
     * methods available to subclasses.
     */
    protected EntityManager em;

    /**
     * Injected session context.
     */
    private SessionContext sctx;

    /**
     * Gets the principal being used to execute the DAO.
     *
     * @return principal
     */
    protected Principal getPrincipal() {

        return sctx.getCallerPrincipal();
    }

    /**
     * Performs an upsert operation. If the id is {@code null} then
     * {@link EntityManager#persist(Object)} is called otherwise a
     * {@link EntityManager#merge(Object)} is called.
     *
     * @param e
     *            entity
     * @return managed entity.
     */
    public BaseEntity save(final BaseEntity e) {

        if (e.getId() == null) {
            em.persist(e);
            return e;
        } else {
            return em.merge(e);
        }
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
