package net.trajano.jee.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import net.trajano.jee.domain.constraint.CanadianSin;
import net.trajano.jee.domain.dao.ParticipantDAO;
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

    private List<Participant> list;

    private Participant participant = new Participant();

    @Inject
    private transient ParticipantDAO participantDAO;

    /**
     * Input version of the SIN. This allows for spaces and symbols on input it
     * will be stripped off when persisting to the participant.
     */
    @CanadianSin(stripSpacesAndSymbols = true)
    @NotNull
    private String participantSinInput;

    public void add() {

        participant.setSin(participantSinInput.replaceAll("[\\s\\-]", ""));
        participantDAO.save(participant);
        init();
    }

    public void delete(final Participant participant) {

        participant.cancel();
        participantDAO.save(participant);
        init();
    }

    public void edit(final Participant participant) {

        this.participant = participant;
        edit = true;
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
    }

    public boolean isInEdit() {

        return edit;
    }

    public void saveParticipant() {

        participantDAO.save(participant);
        init();
        edit = false;
    }

    public void setParticipantSinInput(final String participantSinInput) {

        this.participantSinInput = participantSinInput;
    }
}
