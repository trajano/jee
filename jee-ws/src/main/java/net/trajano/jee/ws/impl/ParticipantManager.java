package net.trajano.jee.ws.impl;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.jws.WebService;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.ws.ParticipantManagerService;

@WebService(endpointInterface = "net.trajano.jee.ws.ParticipantManagerService",
    targetNamespace = "http://ws.jee.trajano.net/",
    portName = "ParticipantManagerServicePort",
    wsdlLocation = "WEB-INF/wsdl/ParticipantManager.wsdl")
@Dependent
public class ParticipantManager implements
    ParticipantManagerService {

    @Inject
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

}
