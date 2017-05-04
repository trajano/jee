package net.trajano.jee.domain.dao;

import java.security.Principal;

import javax.ejb.Local;

import net.trajano.jee.domain.entity.User;

@Local
public interface UserDAO {

    User getByPrincipal(Principal p);

    User getByUsername(String username);

    boolean isUsernameExist(String username);

}
