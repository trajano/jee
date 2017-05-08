package net.trajano.jee.domain.dao;

import java.util.List;

import javax.ejb.Local;

import net.trajano.jee.domain.entity.Participant;

@Local
public interface ParticipantDAO {

    List<Participant> getAll();

    Participant getBySin(String sin);

    Participant save(Participant participant);
}
