package net.trajano.jee.ws.test;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.ws.ParticipantManagerImpl;

public class ParticipantManagerTest {

    @Test
    public void testGetBySin() {

        final ParticipantManagerImpl participantManagerImpl = new ParticipantManagerImpl();
        participantManagerImpl.setParticipantDAO(Mockito.mock(ParticipantDAO.class));
        assertNull(participantManagerImpl.getBySin("185029154"));
    }
}
