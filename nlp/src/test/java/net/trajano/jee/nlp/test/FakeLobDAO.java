package net.trajano.jee.nlp.test;

import java.util.HashMap;
import java.util.Map;

import net.trajano.jee.domain.dao.LobDAO;

/**
 * This is a non-persistent LobDAO implementation used for testing.
 *
 * @author Archimedes Trajano
 */
public class FakeLobDAO implements
    LobDAO {

    private final Map<Long, byte[]> store = new HashMap<>();

    @Override
    public byte[] get(final long id) {

        return store.get(id);
    }

    @Override
    public void set(final long id,
        final byte[] data) {

        store.put(id, data);

    }

}
