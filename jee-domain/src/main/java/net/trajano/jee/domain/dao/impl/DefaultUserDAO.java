package net.trajano.jee.domain.dao.impl;

import java.security.Principal;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.domain.entity.User;

@Local
@Stateless
public class DefaultUserDAO extends BaseDAO<User> implements
    UserDAO {

    /**
     * {@code username} parameter.
     */
    private static final String PARAM_USERNAME = "username";

    @Override
    public User getByPrincipal(final Principal p) {

        return getByUsername(p.getName());
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
