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

            try (final PreparedStatement stmt = c.prepareStatement("select CHUNK, LASTCHUNK from LOBDATA where NAME = ? order by CHUNKSEQUENCE")) {
                stmt.setString(1, name);
                try (final ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }
                    final CompositeInputStream.Builder builder = new CompositeInputStream.Builder();
                    do {
                        builder.addStream(rs.getBlob(1).getBinaryStream());
                        if (rs.getBoolean(2)) {
                            break;
                        }
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

    @Override
    public void update(final String name,
        final InputStream is) {

        try (final Connection c = ds.getConnection()) {
            try (final PreparedStatement deleteStmt = c.prepareStatement("delete from LOBDATA where NAME = ?")) {
                deleteStmt.setString(1, name);
                deleteStmt.executeUpdate();
            }
            try (final PreparedStatement insertStmt = c.prepareStatement("insert INTO LOBDATA (NAME, CHUNKSEQUENCE, CHUNK, LASTCHUNK, LASTUPDATEDON) values (?,?,?,?,?)")) {
                final byte[] chunk = new byte[LobData.CHUNK_SIZE];
                try (final BufferedInputStream stream = new BufferedInputStream(is)) {
                    int seq = 0;
                    for (;;) {
                        final int length = stream.read(chunk);

                        if (length == -1) {
                            break;
                        } else {
                            insertStmt.setString(1, name);
                            insertStmt.setInt(2, seq);
                            insertStmt.setBlob(3, new ByteArrayInputStream(chunk, 0, length));
                            insertStmt.setBoolean(4, false);
                            insertStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                            insertStmt.executeUpdate();
                            ++seq;
                        }
                    }

                }
            }
        } catch (final IOException
            | SQLException e) {
            throw new PersistenceException(e);
        }
    }

}
