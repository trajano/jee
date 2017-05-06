package net.trajano.jee.domain.dao.impl;

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
     * Sets/injects the entity manager.
     *
     * @param em
     *            entity manager
     */
    @PersistenceContext
    public void setEntityManager(final EntityManager em) {

        this.em = em;
    }

    /**
     * Sets/injects the session context.
     *
     * @param sctx
     *            session context.
     */
    @Resource
    public void setSessionContext(final SessionContext sctx) {

        this.sctx = sctx;
    }

}
