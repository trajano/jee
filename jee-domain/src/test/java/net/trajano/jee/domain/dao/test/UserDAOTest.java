package net.trajano.jee.domain.dao.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;

import net.trajano.jee.domain.dao.UserDAO;
import net.trajano.jee.domain.entity.User;

public class UserDAOTest extends BaseIntegrationTest {

    private UserDAO dao;

    @Before
    public void buildDaoAndSampleUser() {

        final User newUser = new User("trajano");
        em.persist(newUser);
        em.flush();
        dao = container.select(UserDAO.class).get();
    }

    @Test
    public void jpaTests() throws Exception {

        assertFalse(dao.isUsernameExist("notexist"));
        assertTrue(dao.isUsernameExist("trajano"));
        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("trajano");
        assertNotNull(dao.getByPrincipal(principal));
    }
}
