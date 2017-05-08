package net.trajano.jee.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.trajano.jee.domain.dao.ParticipantDAO;
import net.trajano.jee.domain.dao.UserDAO;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 2314763538891384789L;

    @Inject
    private transient ParticipantDAO participantDAO;

    @Inject
    private transient UserDAO userDAO;

    @Override
    protected void doGet(final HttpServletRequest req,
        final HttpServletResponse resp) throws ServletException,
        IOException {

        try {
            resp.setContentType("text/plain");
            final PrintWriter writer = resp.getWriter();
            writer.println("Hello " + req.getUserPrincipal().getName() + " at " + new Date());
            writer.println("user is " + userDAO.getByUsername("trajano"));
            writer.println(participantDAO.getAll());
            writer.println(req.isUserInRole("users"));
            writer.println(req.isUserInRole("appuser"));
            writer.println(req.isUserInRole("appusers"));
        } catch (final IOException e) {
            log("IO Exception", e);
        }
    }
}
