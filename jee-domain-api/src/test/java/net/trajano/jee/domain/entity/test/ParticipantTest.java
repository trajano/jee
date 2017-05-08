package net.trajano.jee.domain.entity.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import net.trajano.jee.domain.entity.Gender;
import net.trajano.jee.domain.entity.Participant;

public class ParticipantTest {

    @Test
    public void testParticipant() {

        final Participant p = new Participant();
        assertFalse(p.isAssigned());
        p.setGenderAtBirth(Gender.NOT_DISCLOSED);
        assertEquals(Gender.NOT_DISCLOSED, p.getGenderAtBirth());
    }
}
