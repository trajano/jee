package net.trajano.jee.domain.dao.impl;

import java.security.Principal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.TypedQuery;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.domain.entity.User;

@Local
@Stateless
@Dependent
public class DefaultUserDAO extends BaseDAO implements
    UserDAO {

    /**
     * {@code username} parameter.
     */
    private static final String PARAM_USERNAME = "username";

    @Override
    public User getByPrincipal(final Principal p) {

        final TypedQuery<User> q = em.createNamedQuery(NamedQueries.USER_GET_BY_USERNAME, User.class);
        q.setParameter(PARAM_USERNAME, p.getName());
        return q.getSingleResult();
    }

    @Override
    public User getByUsername(final String username) {

        final TypedQuery<User> q = em.createNamedQuery(NamedQueries.USER_GET_BY_USERNAME, User.class);
        q.setParameter(PARAM_USERNAME, username);
        return q.getSingleResult();
    }

    @Override
    public boolean isUsernameExist(final String username) {

        final TypedQuery<Long> q = em.createNamedQuery(NamedQueries.USER_COUNT_BY_USERNAME, Long.class);
        q.setParameter(PARAM_USERNAME, username);
        return q.getSingleResult().longValue() == 1L;
    }

}
