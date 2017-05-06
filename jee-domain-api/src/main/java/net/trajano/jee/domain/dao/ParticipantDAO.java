package net.trajano.jee.domain.dao;

import java.util.List;

import javax.ejb.Local;

import net.trajano.jee.domain.entity.Participant;

@Local
public interface ParticipantDAO {

    Participant get(long id);

    List<Participant> getAll();

    Participant save(Participant participant);
}
