package net.trajano.jee.domain.dao.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    @Override
    public InputStream getInputStream(final String name) {

        try (final Connection c = ds.getConnection()) {

            try (final PreparedStatement stmt = c.prepareStatement("select CHUNK from LOBDATA where NAME = ? order by CHUNKSEQUENCE")) {
                stmt.setString(1, name);
                try (final ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    final CompositeInputStream.Builder builder = new CompositeInputStream.Builder();
                    do {
                        builder.addStream(rs.getBlob(1).getBinaryStream());
                    } while (rs.next());
                    return builder.build();

                }
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(final String name) {

        try (final Connection c = ds.getConnection()) {
            try (final PreparedStatement stmt = c.prepareStatement("delete from LOBDATA where NAME = ?")) {
                stmt.setString(1, name);
                stmt.executeUpdate();
            }
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Inject
    public void setDataSource(final DataSource ds) {

        this.ds = ds;

    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates are implemented as follows:
     * </p>
     * <ol>
     * <li>Select to find all the entries for the name ordered by sequence</li>
     * <li>For each chunk
     * <ol>
     * <li>if there are still more records:
     * <ol>
     * <li>update the record</li>
     * <li>set last chunk = true if last chunk</li>
     * </ol>
     * <li>else
     * <ol>
     * <li>insert a new record</li>
     * <li>set last chunk = true if last chunk</li>
     * </ol>
     * </li></li>
     * </ol>
     * </ol>
     */
    @Override
    public void update(final String name,
        final InputStream is) {

        try (final Connection c = ds.getConnection()) {
            try (
                final PreparedStatement selectStmt = c.prepareStatement("SELECT NAME, CHUNKSEQUENCE, CHUNK, LASTUPDATEDON FROM LOBDATA where NAME = ? order by CHUNKSEQUENCE", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                final PreparedStatement insertStmt = c.prepareStatement("insert INTO LOBDATA (NAME, CHUNKSEQUENCE, CHUNK, LASTUPDATEDON) values (?,?,?,?)")) {
                final Timestamp ts = new Timestamp(System.currentTimeMillis());

                selectStmt.setString(1, name);

                try (final ResultSet rs = selectStmt.executeQuery()) {

                    final byte[] chunk = new byte[LobData.CHUNK_SIZE];
                    try (final BufferedInputStream stream = new BufferedInputStream(is, LobData.CHUNK_SIZE)) {
                        int seq = 0;
                        boolean startInserting = false;
                        for (;;) {
                            final int length = stream.read(chunk);

                            if (length == -1) {
                                break;
                            }

                            if (!startInserting && !rs.next()) {
                                startInserting = true;
                                insertStmt.setString(1, name);
                                insertStmt.setTimestamp(4, ts);
                            }

                            if (startInserting) {
                                insertStmt.setInt(2, seq);
                                insertStmt.setBlob(3, new ByteArrayInputStream(chunk, 0, length));
                                insertStmt.executeUpdate();
                                ++seq;
                            } else {
                                seq = rs.getInt(2) + 1;
                                rs.updateBinaryStream(3, new ByteArrayInputStream(chunk, 0, length));
                                rs.updateTimestamp(4, ts);
                                rs.updateRow();
                            }
                        }

                    }
                    while (rs.next()) {
                        rs.deleteRow();
                    }
                }

            }
        } catch (final IOException
            | SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
