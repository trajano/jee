package net.trajano.jee.domain.dao;

import java.util.List;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

import net.trajano.jee.domain.constraint.CanadianSin;
import net.trajano.jee.domain.entity.Participant;

@Local
public interface ParticipantDAO {

    List<Participant> getAll();

    Participant getBySin(@NotNull @CanadianSin String sin);

    Participant save(@NotNull Participant participant);
}
