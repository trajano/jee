package net.trajano.jee.web.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.faces.config.InitFacesContext;

import net.trajano.jee.bean.ParticipantBean;
import net.trajano.jee.domain.constraint.CanadianSinValidator;
import net.trajano.jee.domain.dao.DuplicateSinException;
import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Gender;
import net.trajano.jee.domain.entity.Participant;

public class ParticipantBeanTest {

    private class FakeContext extends InitFacesContext {

        public FakeContext(final ServletContext sc) {
            super(sc);
            setCurrentInstance(context);
        }

    }

    @Mock
    private FacesContext context;

    @Before
    public void before() {

        MockitoAnnotations.initMocks(this);
        final ServletContext sc = mock(ServletContext.class);
        new FakeContext(sc);
        assertEquals(context, FacesContext.getCurrentInstance());
    }

    @Test
    public void testFillSample() throws Exception {

        final ParticipantBean bean = new ParticipantBean();
        final String sin = bean.getGeneratedSin();
        bean.fillSample(sin);
        assertEquals(Gender.MALE, bean.getGenderAtBirthInput());
        assertEquals(sin, bean.getParticipantSinInput());

    }

    @Test
    public void testParticipantBean() throws Exception {

        final ParticipantBean bean = new ParticipantBean();
        bean.setParticipantDAO(mock(ParticipantDAO.class));
        bean.init();
        assertTrue(new CanadianSinValidator().isValid(bean.getGeneratedSin(), null));
        assertFalse(bean.isInEdit());
        final Participant participant = new Participant();
        participant.setGenderAtBirth(Gender.NOT_DISCLOSED);
        final String sin = CanadianSinValidator.generate();
        participant.setSin(sin);
        bean.edit(participant);
        assertTrue(bean.isInEdit());
        assertEquals(Gender.NOT_DISCLOSED, bean.getGenderAtBirthInput());
        assertEquals(participant, bean.getParticipant());
        bean.saveParticipant();
        assertFalse(bean.isInEdit());

    }

    @Test
    public void testParticipantBeanAdd() throws Exception {

        final ParticipantBean bean = new ParticipantBean();
        final ParticipantDAO dao = mock(ParticipantDAO.class);
        bean.setParticipantDAO(dao);
        bean.init();
        bean.setGenderAtBirthInput(Gender.FEMALE);
        final String sin = CanadianSinValidator.generate();
        bean.setParticipantSinInput("           " + sin + "           ");
        final ArgumentCaptor<Participant> participantCaptor = ArgumentCaptor.forClass(Participant.class);
        bean.add();
        verify(dao).save(participantCaptor.capture());
        assertEquals(sin, participantCaptor.getValue().getSin());
        assertEquals(Gender.FEMALE, participantCaptor.getValue().getGenderAtBirth());
        assertFalse(bean.isInEdit());

    }

    @Test
    public void testParticipantBeanAddFailedDuplicate() throws Exception {

        final ParticipantBean bean = new ParticipantBean();
        final ParticipantDAO dao = mock(ParticipantDAO.class);
        when(dao.save(any(Participant.class))).thenThrow(new DuplicateSinException());
        bean.setParticipantDAO(dao);
        bean.init();
        bean.add();
        assertFalse(bean.isInEdit());

    }

    @Test
    public void testParticipantBeanCancel() throws Exception {

        final ParticipantBean bean = new ParticipantBean();
        bean.setParticipantDAO(mock(ParticipantDAO.class));
        bean.init();
        bean.delete(new Participant());
        assertFalse(bean.isInEdit());

    }
}
