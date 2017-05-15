package net.trajano.jee.domain.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import net.trajano.jee.domain.dao.LobDAO;
import net.trajano.jee.domain.dao.impl.DefaultLobDAO;

public class LobDAOTest extends BaseIntegrationTest {

    private LobDAO dao;

    @Before
    public void buildDao() {

        final DefaultLobDAO dao = new DefaultLobDAO();
        dao.setEntityManager(em);
        this.dao = dao;
    }

    @Test
    public void testDaoOperations() {

        final byte[] testData = new byte[] {
            0,
            1,
            2,
            3,
            4
        };
        assertNull(dao.get(256));
        dao.set(256, testData);
        assertEquals(testData, dao.get(256));
    }

}
