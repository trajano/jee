package net.trajano.jee.web.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import net.trajano.jee.bean.ParticipantBean;
import net.trajano.jee.domain.constraint.CanadianSinValidator;
import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Participant;

public class ParticipantBeanTest {

    @Test
    public void testParticipantBean() throws Exception {

        final ParticipantBean bean = new ParticipantBean();
        bean.setParticipantDAO(mock(ParticipantDAO.class));
        bean.init();
        assertTrue(new CanadianSinValidator().isValid(bean.getGeneratedSin(), null));
        assertFalse(bean.isInEdit());
        bean.edit(new Participant());
        assertTrue(bean.isInEdit());
        bean.saveParticipant();
        assertFalse(bean.isInEdit());

    }
}
