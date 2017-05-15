package net.trajano.jee.domain.dao.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;

import net.trajano.jee.domain.dao.LobDAO;

/**
 * Provides common methods and data to DAOs.
 *
 * @author Archimedes Trajano
 */
@Local
@Stateless
@Dependent
public class DefaultLobDAO implements
    LobDAO {

    private DataSource ds;

    /**
     * Injected entity manager.
     */
    private EntityManager em;

    @Override
    public byte[] get(final long id) {

        final LobData entity = em.find(LobData.class, id);
        if (entity == null) {
            return null;
        } else {
            return entity.getLobData();
        }
    }

    @Override
    public InputStream getInputStream(final long id) {

        try (final Connection c = ds.getConnection()) {

            try (final PreparedStatement stmt = c.prepareStatement("select LOBDATA from LOBDATA where ID = ?")) {
                stmt.setLong(1, id);
                try (final ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    } else {
                        return rs.getBlob(1).getBinaryStream();
                    }
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(final long id) {

        try (final Connection c = ds.getConnection()) {
            try (final PreparedStatement stmt = c.prepareStatement("delete from LOBDATA where ID = ?")) {
                stmt.setLong(1, id);
                if (stmt.executeUpdate() != 1) {
                    throw new SQLException("delete failed");
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
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

    @Inject
    public void setDataSource(final DataSource ds) {

        this.ds = ds;

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

    @Override
    public void update(final long id,
        final InputStream is) {

        try (final Connection c = ds.getConnection()) {
            try (final PreparedStatement stmt = c.prepareStatement("select LOBDATA, LASTUPDATEDON from LOBDATA where ID = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                final PreparedStatement insertStmt = c.prepareStatement("insert INTO LOBDATA (ID, LOBDATA, LASTUPDATEDON) values (?,?,?)")) {
                stmt.setLong(1, id);
                try (final ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        rs.updateBinaryStream(1, is);
                        rs.updateTimestamp(2, new Timestamp(System.currentTimeMillis()));
                    } else {
                        insertStmt.setLong(1, id);
                        insertStmt.setBinaryStream(2, is);
                        insertStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                        if (insertStmt.executeUpdate() != 1) {
                            throw new SQLException("Insert failed");
                        }
                    }
                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
