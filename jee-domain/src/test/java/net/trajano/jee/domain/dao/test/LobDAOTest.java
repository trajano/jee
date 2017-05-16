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

        Arrays.fill(testData, (byte) 65);
        dao.update("group1", new ByteArrayInputStream(testData));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBuffer);
            assertArrayEquals(testDataBuffer, testData);
        }

        dao.remove("group1");
        assertNull(dao.getInputStream("group1"));
    }

    @Test
    public void testLargeBlobVarySize() throws Exception {

        final byte[] testData = new byte[2 * 1024 * 1024];
        final byte[] testDataPlus1 = new byte[2 * 1024 * 1024 + 1];
        final byte[] testDataPlusLots = new byte[4 * 1024 * 1024];
        final byte[] testDataMinus1 = new byte[2 * 1024 * 1024 - 1];
        Arrays.fill(testData, (byte) 64);
        Arrays.fill(testDataPlus1, (byte) 65);
        Arrays.fill(testDataPlusLots, (byte) 120);
        Arrays.fill(testDataMinus1, (byte) 63);

        final byte[] testDataBuffer = new byte[testData.length];
        final byte[] testDataBufferPlus1 = new byte[testDataPlus1.length];
        final byte[] testDataBufferPlusLots = new byte[testDataPlusLots.length];
        final byte[] testDataBufferMinus1 = new byte[testDataMinus1.length];

        assertNull(dao.getInputStream("group1"));
        dao.update("group1", new ByteArrayInputStream(testData));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBuffer);
            assertArrayEquals(testDataBuffer, testData);
        }

        dao.update("group1", new ByteArrayInputStream(testDataBufferPlus1));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBufferPlus1);
            assertArrayEquals(testDataBufferPlus1, testDataBufferPlus1);
        }

        dao.update("group1", new ByteArrayInputStream(testDataMinus1));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBufferMinus1);
            assertArrayEquals(testDataBufferMinus1, testDataMinus1);
        }

        dao.update("group1", new ByteArrayInputStream(testDataBufferPlus1));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBufferPlus1);
            assertArrayEquals(testDataBufferPlus1, testDataBufferPlus1);
        }
        dao.update("group1", new ByteArrayInputStream(testDataPlusLots));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBufferPlusLots);
            assertArrayEquals(testDataBufferPlusLots, testDataPlusLots);
        }
        dao.update("group1", new ByteArrayInputStream(testDataMinus1));
        {
            final InputStream inputStream = dao.getInputStream("group1");
            new DataInputStream(inputStream).readFully(testDataBufferMinus1);
            assertArrayEquals(testDataBufferMinus1, testDataMinus1);
        }

        dao.remove("group1");
        assertNull(dao.getInputStream("group1"));
    }
}
