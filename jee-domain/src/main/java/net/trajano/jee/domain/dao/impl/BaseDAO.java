package net.trajano.jee.domain.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.trajano.jee.domain.entity.BaseEntity;

/**
 * Provides common methods and data to DAOs.
 *
 * @author Archimedes Trajano
 */
public class BaseDAO<T extends BaseEntity> {

    /**
     * Injected entity manager. Made protected to allow the whole gamut of
     * methods available to subclasses.
     */
    protected EntityManager em;

    /**
     * This will execute a {@link TypedQuery#getSingleResult()} and catch a
     * {@link NoResultException} and return {@code null} that situation.
     *
     * @param q
     *            query
     * @return single result or null
     */
    protected T nullIfNotFound(final TypedQuery<T> q) {

        try {
            return q.getSingleResult();
        } catch (final NoResultException e) {
            return null;
        }
    }

    /**
     * Performs an upsert operation to store the data. If the id is {@code null}
     * then {@link EntityManager#persist(Object)} is called otherwise a
     * {@link EntityManager#merge(Object)} is called.
     *
     * @param e
     *            entity
     * @return managed entity.
     */
    public T save(final T e) {

        if (e.isAssigned()) {
            return em.merge(e);
        } else {
            em.persist(e);
            return e;
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
