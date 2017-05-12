package net.trajano.jee.ws.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import net.trajano.jee.domain.constraint.CanadianSinValidator;
import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Gender;
import net.trajano.jee.domain.entity.Participant;
import net.trajano.jee.ws.ParticipantManagerImpl;

public class ParticipantManagerTest {

    @Test
    public void testGetAll() {

        final ParticipantManagerImpl participantManagerImpl = new ParticipantManagerImpl();
        final ParticipantDAO dao = Mockito.mock(ParticipantDAO.class);
        final List<Participant> participants = new ArrayList<>();
        {
            final Participant participant = new Participant();
            participant.setSin(CanadianSinValidator.generate());
            participant.setEmail("asdf@jkl.semi");
            participant.setGenderAtBirth(Gender.MALE);
            participants.add(participant);
        }
        {
            final Participant participant = new Participant();
            participant.setSin(CanadianSinValidator.generate());
            participant.setEmail("asdf@jkl.org");
            participant.setGenderAtBirth(Gender.FEMALE);
            participants.add(participant);
        }
        when(dao.getAll()).thenReturn(participants);
        participantManagerImpl.setParticipantDAO(dao);

        final List<net.trajano.jee.schema.Participant> all = participantManagerImpl.getAll();
        assertEquals(2, all.size());
    }

    @Test
    public void testGetAllEmpty() {

        final ParticipantManagerImpl participantManagerImpl = new ParticipantManagerImpl();
        final ParticipantDAO dao = Mockito.mock(ParticipantDAO.class);
        participantManagerImpl.setParticipantDAO(dao);

        final List<net.trajano.jee.schema.Participant> all = participantManagerImpl.getAll();
        assertTrue(all.isEmpty());
    }

    @Test
    public void testGetBySin() {

        final ParticipantManagerImpl participantManagerImpl = new ParticipantManagerImpl();
        final ParticipantDAO dao = Mockito.mock(ParticipantDAO.class);

        final Participant participant = new Participant();
        participant.setSin("185029154");
        participant.setEmail("asdf@jkl.semi");
        participant.setGenderAtBirth(Gender.NOT_DISCLOSED);
        when(dao.getBySin("185029154")).thenReturn(participant);

        participantManagerImpl.setParticipantDAO(dao);

        final net.trajano.jee.schema.Participant bySin = participantManagerImpl.getBySin("185029154");
        assertNotNull(bySin);
        assertEquals("185029154", bySin.getSin());
        assertEquals("asdf@jkl.semi", bySin.getEmail());
        assertEquals(net.trajano.jee.schema.Gender.NOT_DISCLOSED, bySin.getGenderAtBirth());
    }

    @Test
    public void testGetBySinNotFound() {

        final ParticipantManagerImpl participantManagerImpl = new ParticipantManagerImpl();
        participantManagerImpl.setParticipantDAO(Mockito.mock(ParticipantDAO.class));
        assertNull(participantManagerImpl.getBySin("185029154"));
    }
}
