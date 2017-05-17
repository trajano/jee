package net.trajano.jee.ws.test;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.mockito.Mockito;

import net.trajano.jee.domain.dao.LobDAO;
import net.trajano.jee.schema.LobData;
import net.trajano.jee.ws.LobManagerImpl;

public class LobManagerTest {

    @Test
    public void testSaveLob() {

        final LobManagerImpl impl = new LobManagerImpl();
        final LobDAO dao = Mockito.mock(LobDAO.class);
        impl.setLobDAO(dao);
        final LobData lobData = new LobData();
        lobData.setName("hello");
        lobData.setData("world".getBytes(StandardCharsets.UTF_8));
        impl.saveLobData(lobData);

    }

}
