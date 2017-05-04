package net.trajano.jee.domain.dao.internal;

import java.security.Principal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.domain.entity.User;

@Local
@Stateless
@Dependent
public class DefaultUserDAO implements
    UserDAO {

    private EntityManager em;

    @Override
    public User getByPrincipal(final Principal p) {

        final TypedQuery<User> q = em.createNamedQuery(NamedQueries.USER_GET_BY_USERNAME, User.class);
        q.setParameter("username", p.getName());
        return q.getSingleResult();
    }

    @Override
    public User getByUsername(final String username) {

        final TypedQuery<User> q = em.createNamedQuery(NamedQueries.USER_GET_BY_USERNAME, User.class);
        q.setParameter("username", username);
        return q.getSingleResult();
    }

    @PersistenceContext
    public void setEntityManager(final EntityManager em) {

        this.em = em;
    }
}
