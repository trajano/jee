package net.trajano.jee.domain.dao.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import net.trajano.jee.domain.dao.LobDAO;

/**
 * Provides common methods and data to DAOs.
 *
 * @author Archimedes Trajano
 */
public class DefaultLobDAO implements
    LobDAO {

    /**
     * Injected entity manager. Made protected to allow the whole gamut of
     * methods available to subclasses.
     */
    protected EntityManager em;

    @Override
    public byte[] get(final long id) {

        final LobData entity = em.find(LobData.class, id);
        if (entity == null) {
            return null;
        } else {
            return entity.getLobData();
        }
    }

    /**
     * Performs an upsert operation to store the data. If the id is {@code null}
     * then {@link EntityManager#persist(Object)} is called otherwise a
     * {@link EntityManager#merge(Object)} is called.
     *
     * @param id
     *            id
     * @param data
     *            LOB data
     */
    @Override
    public void set(final long id,
        final byte[] data) {

        final LobData entity = em.find(LobData.class, id);
        if (entity == null) {
            em.persist(new LobData(id, data));
        } else {
            entity.setLobData(data);
            em.merge(entity);
        }

    }

    /**
     * Sets/injects the entity manager.
     *
     * @param em
     *            entity manager
     */
    @Inject
    public void setEntityManager(final EntityManager em) {

        this.em = em;
    }

}
