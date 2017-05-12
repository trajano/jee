package net.trajano.jee.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import net.trajano.jee.domain.constraint.CanadianSin;
import net.trajano.jee.domain.constraint.CanadianSinValidator;
import net.trajano.jee.domain.dao.DuplicateSinException;
import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Gender;
import net.trajano.jee.domain.entity.Participant;

@Named
@ViewScoped
public class ParticipantBean implements
    Serializable {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 5615914274881003121L;

    private boolean edit;

    /**
     * Gender at birth input.
     */
    private Gender genderAtBirthInput;

    private List<Participant> list;

    private Participant participant = new Participant();

    private transient ParticipantDAO participantDAO;

    /**
     * Input version of the SIN. This allows for spaces and symbols on input it
     * will be stripped off when persisting to the participant.
     */
    @CanadianSin(relaxed = true)
    @NotNull
    private String participantSinInput;

    public void add() {

        participant.setSin(participantSinInput.replaceAll("[\\s\\-]", ""));
        participant.setGenderAtBirth(genderAtBirthInput);
        try {
            participantDAO.save(participant);
            init();
        } catch (final DuplicateSinException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate SIN", "Duplicate SIN"));
        }
    }

    public void delete(final Participant participant) {

        participant.cancel();
        participantDAO.save(participant);
        init();
    }

    public void edit(final Participant participant) {

        this.participant = participant;
        participantSinInput = participant.getSin();
        genderAtBirthInput = participant.getGenderAtBirth();
        edit = true;
    }

    public Gender getGenderAtBirthInput() {

        return genderAtBirthInput;
    }

    public String getGeneratedSin() {

        return CanadianSinValidator.generate();
    }

    public List<Participant> getList() {

        return list;
    }

    public Participant getParticipant() {

        return participant;
    }

    public String getParticipantSinInput() {

        return participantSinInput;
    }

    @PostConstruct
    public void init() {

        list = participantDAO.getAll();
        // Reset placeholder.
        participant = new Participant();
        participantSinInput = "";
        genderAtBirthInput = null;
    }

    public boolean isInEdit() {

        return edit;
    }

    public void saveParticipant() {

        participant.setGenderAtBirth(genderAtBirthInput);
        participantDAO.save(participant);
        init();
        edit = false;
    }

    public void setGenderAtBirthInput(final Gender genderAtBirthInput) {

        this.genderAtBirthInput = genderAtBirthInput;
    }

    @Inject
    public void setParticipantDAO(final ParticipantDAO participantDAO) {

        this.participantDAO = participantDAO;
    }

    public void setParticipantSinInput(final String participantSinInput) {

        this.participantSinInput = participantSinInput;
    }
}
