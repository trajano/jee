package net.trajano.jee.domain.dao.test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.PersistenceException;
import javax.sql.DataSource;

import org.junit.Test;

import net.trajano.jee.domain.dao.impl.DefaultLobDAO;

/**
 * Tests LobDAO with mocks that can throw errors when wanted.
 *
 * @author Archimedes Trajano
 */
public class MockedLobDAOTest {

    @Test(expected = PersistenceException.class)
    public void badGetStream() throws Exception {

        final DataSource badConnectionDataSource = mock(DataSource.class);
        when(badConnectionDataSource.getConnection()).thenThrow(SQLException.class);
        final DefaultLobDAO dao = new DefaultLobDAO();
        dao.setDataSource(badConnectionDataSource);
        dao.getInputStream("hello");
    }

    @Test(expected = PersistenceException.class)
    public void badRemove() throws Exception {

        final DataSource badConnectionDataSource = mock(DataSource.class);
        when(badConnectionDataSource.getConnection()).thenThrow(SQLException.class);
        final DefaultLobDAO dao = new DefaultLobDAO();
        dao.setDataSource(badConnectionDataSource);
        dao.remove("hello");
    }

    @Test(expected = PersistenceException.class)
    public void badUpdate() throws Exception {

        final DataSource badConnectionDataSource = mock(DataSource.class);
        when(badConnectionDataSource.getConnection()).thenThrow(SQLException.class);
        final DefaultLobDAO dao = new DefaultLobDAO();
        dao.setDataSource(badConnectionDataSource);
        dao.update("hello", new ByteArrayInputStream(new byte[0]));
    }

    @Test
    public void successfulMock() throws Exception {

        final DataSource dataSource = mock(DataSource.class);

        final Connection connection = mock(Connection.class);
        when(connection.prepareStatement(anyString(), eq(ResultSet.TYPE_FORWARD_ONLY))).thenReturn(mock(PreparedStatement.class));
        when(connection.prepareStatement(anyString())).thenReturn(mock(PreparedStatement.class));

        when(dataSource.getConnection()).thenReturn(connection);

        final DefaultLobDAO dao = new DefaultLobDAO();
        dao.setDataSource(dataSource);
        dao.update("hello", new ByteArrayInputStream(new byte[0]));
    }
}
