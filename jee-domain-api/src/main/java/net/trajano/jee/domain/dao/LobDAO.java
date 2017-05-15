package net.trajano.jee.domain.dao;

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
     * Sets the value for the lob data.
     *
     * @param id
     *            id for the lob.
     * @param data
     *            the data
     */
    void set(long id,
        byte[] data);
}
