package net.trajano.jee.ws;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.MTOM;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Participant;

/**
 * This is an example of a code first web service. Not something I would
 * recommend, but ensures the technology works.
 *
 * @author Archimedes Trajano
 */
@MTOM(enabled = true)
@WebService
@Addressing(enabled = true,
    required = true)
public class ParticipantCodeFirst {

    @Inject
    private ParticipantDAO participantDAO;

    @WebMethod(action = "getAll")
    public List<Participant> getAll() {

        return participantDAO.getAll();
    }

}
