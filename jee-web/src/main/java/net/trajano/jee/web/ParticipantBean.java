package net.trajano.jee.web;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Participant;

@ManagedBean
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

    public void add() {

        System.out.println("Calling add");
        participantDAO.save(participant);
        init();
    }

    public void delete(final Participant participant) {

        participant.getAudit().cancel();
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

    @PostConstruct
    public void init() {

        list = participantDAO.getAll();
        participant = new Participant(); // Reset placeholder.
    }

    public boolean isEdit() {

        return edit;
    }

    public void saveParticipant() {

        System.out.println("Calling save");
        participantDAO.save(participant);
        System.out.println("Done Calling save");
        init();
        edit = false;
    }
}
