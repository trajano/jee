package net.trajano.jee.domain.dao;

import java.io.InputStream;
import java.sql.SQLException;

public interface LobDAO {

    /**
     * This may return <code>null</code>.
     *
     * @param id
     *            id for the lob.
     * @return the data
     */
    byte[] get(long id);

    /**
     * This may return <code>null</code>.
     *
     * @param id
     *            id for the lob.
     * @return input stream for the lob data.
     */
    InputStream getInputStream(long id) throws SQLException;

    /**
     * Sets the value for the lob data.
     *
     * @param id
     *            id for the lob.
     * @param data
     *            the data
     */
    void set(long id,
        byte[] data);

    void update(long id,
        InputStream is) throws SQLException;
}
