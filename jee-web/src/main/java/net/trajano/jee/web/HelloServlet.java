package net.trajano.jee.web;

import java.io.IOException;
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

        resp.setContentType("text/plain");
        resp.getWriter().println("Hello " + req.getUserPrincipal().getName() + " at " + new Date());
        resp.getWriter().println("user is " + userDAO.getByUsername("trajano"));
        resp.getWriter().println(participantDAO.getAll());
        resp.getWriter().println(req.isUserInRole("users"));
        resp.getWriter().println(req.isUserInRole("appuser"));
        resp.getWriter().println(req.isUserInRole("appusers"));
    }
}
