package net.trajano.jee.rs;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.trajano.jee.domain.constraint.CanadianSin;
import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.entity.Participant;

@Path("/participants")
@Produces(MediaType.APPLICATION_JSON)
public class Participants {

    @Inject
    private ParticipantDAO participantDAO;

    @GET
    public List<Participant> getAll() {

        return participantDAO.getAll();
    }

    @GET
    @Path("{sin}")
    public Response getOne(@PathParam("sin") @CanadianSin @NotNull final String sin) {

        final Participant participant = participantDAO.getBySin(sin);
        if (participant != null) {
            return Response.ok(participant).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity("Not found").type(MediaType.TEXT_PLAIN).build();
        }

    }
}
