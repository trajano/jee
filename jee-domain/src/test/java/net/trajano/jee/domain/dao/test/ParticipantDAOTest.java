package net.trajano.jee.domain.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.dao.impl.DefaultParticipantDAO;
import net.trajano.jee.domain.entity.Gender;
import net.trajano.jee.domain.entity.Participant;

public class ParticipantDAOTest extends BaseJpaTest {

    private ParticipantDAO dao;

    @Before
    public void buildDao() {

        final DefaultParticipantDAO dao = new DefaultParticipantDAO();
        dao.setEntityManager(em);
        this.dao = dao;
    }

    @Test
    public void testEmptyCollectionAtStart() {

        assertTrue(dao.getAll().isEmpty());
    }

    @Test
    public void testSaveAndGet() {

        final Participant participant = new Participant();
        participant.setName("Archie");
        participant.setGenderAtBirth(Gender.MALE);
        final Participant managedParticipant = dao.save(participant);
        assertNotNull(managedParticipant.getId());
        final Participant retrievedParticipant = dao.get(managedParticipant.getId());
        assertEquals(retrievedParticipant.getId(), managedParticipant.getId());
        assertEquals("Archie", retrievedParticipant.getName());
        assertEquals(1, dao.getAll().size());

        retrievedParticipant.setName("Janet");
        retrievedParticipant.setGenderAtBirth(Gender.FEMALE);
        dao.save(retrievedParticipant);
        final Participant updatedParticipant = dao.get(managedParticipant.getId());
        assertEquals("Janet", updatedParticipant.getName());
        assertEquals(1, dao.getAll().size());

    }
}
