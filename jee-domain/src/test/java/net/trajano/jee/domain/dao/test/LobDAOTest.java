package net.trajano.jee.domain.dao.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import net.trajano.jee.domain.dao.LobDAO;

public class LobDAOTest extends BaseIntegrationTest {

    private LobDAO dao;

    @Before
    public void buildDao() {

        dao = container.select(LobDAO.class).get();
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
        assertArrayEquals(testData, dao.get(256));
    }

    @Test
    public void testDaoStreamOperations() throws Exception {

        final byte[] testData = new byte[] {
            0,
            1,
            2,
            3,
            4
        };
        final byte[] testDataBuffer = new byte[testData.length];
        assertNull(dao.getInputStream(257L));
        dao.update(257L, new ByteArrayInputStream(testData));
        assertArrayEquals(testData, dao.get(257L));
        final InputStream inputStream = dao.getInputStream(257L);
        new DataInputStream(inputStream).readFully(testDataBuffer);
        assertArrayEquals(testDataBuffer, testData);

        dao.remove(257L);
        assertNull(dao.getInputStream(257L));
    }

}
