package net.trajano.jee.ws;

import java.io.ByteArrayInputStream;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.MTOM;

import net.trajano.jee.domain.dao.LobDAO;
import net.trajano.jee.schema.LobData;

@MTOM(enabled = true)
@Addressing(enabled = true,
    required = true)
@WebService(serviceName = "LobManagerService",
    endpointInterface = "net.trajano.jee.ws.LobManager",
    portName = "LobManagerPort",
    wsdlLocation = "WEB-INF/wsdl/LobManager.wsdl")
public class LobManagerImpl implements
    LobManager {

    private LobDAO lobDAO;

    @Override
    public void saveLobData(final LobData lobData) {

        lobDAO.update(lobData.getName(), new ByteArrayInputStream(lobData.getData()));

    }

    @Inject
    public void setLobDAO(final LobDAO lobDAO) {

        this.lobDAO = lobDAO;
    }

}
