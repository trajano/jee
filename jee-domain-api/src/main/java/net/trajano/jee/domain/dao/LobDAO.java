package net.trajano.jee.domain.dao;

import java.io.InputStream;

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
    InputStream getInputStream(long id);

    /**
     * Removes the entry.
     *
     * @param id
     *            ID
     */
    void remove(long id);

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
        InputStream is);
}
