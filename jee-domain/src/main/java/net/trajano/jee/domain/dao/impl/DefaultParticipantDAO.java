package net.trajano.jee.domain.dao.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Participant;

@Local
@Stateless
@Dependent
public class DefaultParticipantDAO extends BaseDAO<Participant> implements
    ParticipantDAO {

    @Override
    public List<Participant> getAll() {

        return em.createNamedQuery(NamedQueries.PARTICIPANT_GET_ALL, Participant.class).getResultList();
    }

    @Override
    public Participant getBySin(@NotNull final String sin) {

        final TypedQuery<Participant> q = em.createNamedQuery(NamedQueries.PARTICIPANT_GET_BY_SIN, Participant.class);
        q.setParameter("sin", sin);
        return nullIfNotFound(q);
    }

}
