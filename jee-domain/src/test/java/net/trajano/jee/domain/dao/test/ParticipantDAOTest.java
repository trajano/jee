package net.trajano.jee.domain.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;

import net.trajano.jee.domain.constraint.CanadianSinValidator;
import net.trajano.jee.domain.dao.DuplicateSinException;
import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.dao.impl.DefaultParticipantDAO;
import net.trajano.jee.domain.entity.Gender;
import net.trajano.jee.domain.entity.Participant;

public class ParticipantDAOTest extends BaseIntegrationTest {

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

    @Test(expected = DuplicateSinException.class)
    public void testFailDuplicateSin() throws Exception {

        final Participant participant = new Participant();
        participant.setName("Archie");
        participant.setGenderAtBirth(Gender.MALE);
        participant.setEmail("foo@com.com");
        final String sin = CanadianSinValidator.generate();
        participant.setSin(sin);
        dao.save(participant);

        final Participant participant2 = new Participant();
        participant2.setName("Archie");
        participant2.setGenderAtBirth(Gender.MALE);
        participant2.setEmail("foo@com.com");
        participant2.setSin(sin);
        dao.save(participant2);
    }

    /**
     * Tests create, read, update, "cancel"
     */
    @Test
    public void testLifecycle() {

        final Participant participant = new Participant();
        participant.setName("Archie");
        participant.setGenderAtBirth(Gender.MALE);
        participant.setEmail("foo@spam.trajano.net");
        final String sin = CanadianSinValidator.generate();
        participant.setSin(sin);
        final Participant managedParticipant = dao.save(participant);
        assertTrue(managedParticipant.isAssigned());
        final Participant retrievedParticipant = dao.getBySin(sin);
        assertEquals(retrievedParticipant.getSin(), sin);
        assertEquals("Archie", retrievedParticipant.getName());
        assertEquals(1, dao.getAll().size());

        retrievedParticipant.setName("Janet");
        retrievedParticipant.setGenderAtBirth(Gender.FEMALE);
        dao.save(retrievedParticipant);
        final Participant updatedParticipant = dao.getBySin(sin);
        assertEquals("Janet", updatedParticipant.getName());
        assertEquals(1, dao.getAll().size());

        updatedParticipant.cancel();
        dao.save(updatedParticipant);

        assertNull(dao.getBySin(sin));

    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveAndGetFailDueToConstraintViolation() {

        final Participant participant = new Participant();
        participant.setName("Archie");
        participant.setGenderAtBirth(Gender.MALE);
        participant.setSin("675828594");
        dao.save(participant);
    }

    @Test(expected = ValidationException.class)
    public void testSaveAndGetFailDueToInvalidSin() {

        final Participant participant = new Participant();
        participant.setName("Archie");
        participant.setSin("675828595");
        participant.setGenderAtBirth(Gender.MALE);
        dao.save(participant);
    }

    @Test(expected = ValidationException.class)
    public void testSaveAndGetFailDueToValidationException() {

        final Participant participant = new Participant();
        participant.setName("Archie");
        participant.setGenderAtBirth(Gender.MALE);
        dao.save(participant);
    }

}
