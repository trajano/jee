package net.trajano.jee.domain.dao;

import java.io.InputStream;

public interface LobDAO {

    /**
     * This may return <code>null</code>.
     *
     * @param name
     *            name of the lob.
     * @return input stream for the lob data.
     */
    InputStream getInputStream(String name);

    /**
     * Removes the entry.
     *
     * @param name
     *            name
     */
    void remove(String name);

    /**
     * Sets the value for the lob data.
     *
     * @param name
     *            id for the lob.
     * @param is
     *            input stream for the data
     */
    void update(String name,
        InputStream is);
}
