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

        try (final Connection c = ds.getConnection();
            final PreparedStatement stmt = c.prepareStatement("select CHUNK from LOBDATA where NAME = ? order by CHUNKSEQUENCE")) {
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
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(final String name) {

        try (final Connection c = ds.getConnection();
            final PreparedStatement stmt = c.prepareStatement("delete from LOBDATA where NAME = ?")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
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
     * </ol>
     * <li>else
     * <ol>
     * <li>insert a new record</li>
     * </ol>
     * </li></li>
     * </ol>
     * <li>Delete other records</li>
     * </ol>
     */
    @Override
    public void update(final String name,
        final InputStream is) {

        try (final Connection c = ds.getConnection();
            final PreparedStatement selectStmt = c.prepareStatement("select CHUNKSEQUENCE from LOBDATA where NAME = ? order by CHUNKSEQUENCE", ResultSet.TYPE_FORWARD_ONLY);
            final PreparedStatement insertStmt = c.prepareStatement("insert into LOBDATA (NAME, CHUNKSEQUENCE, CHUNK, LASTUPDATEDON) values (?,?,?,?)");
            final PreparedStatement updateStmt = c.prepareStatement("update LOBDATA set CHUNK = ?, LASTUPDATEDON = ? where NAME = ? and CHUNKSEQUENCE = ?");
            final PreparedStatement deleteStmt = c.prepareStatement("delete from LOBDATA where NAME = ? and CHUNKSEQUENCE >= ?")) {

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
                            updateStmt.setBlob(1, new ByteArrayInputStream(chunk, 0, length));
                            updateStmt.setTimestamp(2, ts);
                            updateStmt.setString(3, name);
                            updateStmt.setInt(4, seq);
                            updateStmt.executeUpdate();
                            seq = rs.getInt(1) + 1;
                        }
                    }

                    deleteStmt.setString(1, name);
                    deleteStmt.setInt(2, seq);
                    deleteStmt.executeUpdate();
                }
            }

        } catch (final IOException
            | SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
