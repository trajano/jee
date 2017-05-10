package net.trajano.jee.ws;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;

import net.trajano.jee.domain.dao.ParticipantDAO;

@MTOM(enabled = true)
@WebService(serviceName = "ParticipantManagerService",
    endpointInterface = "net.trajano.jee.ws.ParticipantManager",
    portName = "ParticipantManagerPort",
    wsdlLocation = "WEB-INF/wsdl/ParticipantManager.wsdl")
public class ParticipantManagerImpl implements
    ParticipantManager {

    private ParticipantDAO participantDAO;

    @Override
    public List<net.trajano.jee.schema.Participant> getAll() {

        final List<net.trajano.jee.schema.Participant> ret = new LinkedList<>();
        for (final net.trajano.jee.domain.entity.Participant p : participantDAO.getAll()) {
            final net.trajano.jee.schema.Participant schemaParticipant = mapDomainToSchema(p);
            ret.add(schemaParticipant);
        }
        return ret;
    }

    @Override
    public net.trajano.jee.schema.Participant getBySin(final String sin) {

        return mapDomainToSchema(participantDAO.getBySin(sin));
    }

    /**
     * Maps from domain object to schema object.
     *
     * @param p
     *            domain object
     * @return schema object
     */
    private net.trajano.jee.schema.Participant mapDomainToSchema(final net.trajano.jee.domain.entity.Participant p) {

        if (p == null) {
            return null;
        }
        final net.trajano.jee.schema.Participant schemaParticipant = new net.trajano.jee.schema.Participant();
        schemaParticipant.setEmail(p.getEmail());
        schemaParticipant.setSin(p.getSin());
        schemaParticipant.setName(p.getName());
        schemaParticipant.setGenderAtBirth(mapGender(p.getGenderAtBirth()));
        return schemaParticipant;
    }

    /**
     * Maps from domain gender to schema gender
     *
     * @param domainGender
     *            domain gender
     * @return schema gender
     */
    private net.trajano.jee.schema.Gender mapGender(final net.trajano.jee.domain.entity.Gender domainGender) {

        switch (domainGender) {
        case MALE:
            return net.trajano.jee.schema.Gender.MALE;
        case FEMALE:
            return net.trajano.jee.schema.Gender.FEMALE;
        case NOT_DISCLOSED:
            return net.trajano.jee.schema.Gender.NOT_DISCLOSED;
        default:
            throw new IllegalArgumentException(domainGender.name());
        }
    }

    @Inject
    public void setParticipantDAO(final ParticipantDAO participantDAO) {

        this.participantDAO = participantDAO;
    }

}
