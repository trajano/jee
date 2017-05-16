package net.trajano.jee.domain.dao.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Arrays;

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
    public void testDaoStreamOperations() throws Exception {

        final byte[] testData = new byte[] {
            0,
            1,
            2,
            3,
            4
        };
        final byte[] testDataBuffer = new byte[testData.length];
        assertNull(dao.getInputStream("group1"));
        dao.update("group1", new ByteArrayInputStream(testData));

        final InputStream inputStream = dao.getInputStream("group1");
        new DataInputStream(inputStream).readFully(testDataBuffer);
        assertArrayEquals(testDataBuffer, testData);

        dao.remove("group1");
        assertNull(dao.getInputStream("group1"));
    }

    @Test
    public void testLargeBlob() throws Exception {

        final byte[] testData = new byte[2 * 1024 * 1024];
        Arrays.fill(testData, (byte) 64);
        final byte[] testDataBuffer = new byte[testData.length];
        assertNull(dao.getInputStream("group1"));
        dao.update("group1", new ByteArrayInputStream(testData));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBuffer);
            assertArrayEquals(testDataBuffer, testData);
        }

        dao.update("group1", new ByteArrayInputStream(testData));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBuffer);
            assertArrayEquals(testDataBuffer, testData);
        }

        dao.remove("group1");
        assertNull(dao.getInputStream("group1"));
    }
}
