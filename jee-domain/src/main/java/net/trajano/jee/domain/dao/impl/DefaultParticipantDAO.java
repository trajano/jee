package net.trajano.jee.domain.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Participant;

@Local
@Stateless
@Dependent
public class DefaultParticipantDAO extends BaseDAO implements
    ParticipantDAO {

    @Override
    public Participant get(final long id) {

        return em.find(Participant.class, id);
    }

    @Override
    public List<Participant> getAll() {

        return em.createNamedQuery(NamedQueries.PARTICIPANT_GET_ALL, Participant.class).getResultList();
    }

    @Override
    public Participant save(final Participant participant) {

        return (Participant) upsert(participant);
    }

}
