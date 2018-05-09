package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.persistence.Transaction;
import com.excilys.cdb.persistence.exceptions.DaoException;

public class TransactionTest {

    @Mock
    private Connection connection;

    /**
     * Initialisation.
     */
    @Before
    public void initialisation() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test BeginTransaction.
     * @throws SQLException erreur de transaction.
     */
    @Test
    public void testBeginTransactionSetCommitSQLException() throws SQLException {
        Mockito.doThrow(new SQLException()).when(connection).setAutoCommit(false);
        try {
            Transaction.beginTransaction(connection);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(Transaction.MESS_TRANSACTION_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(connection).setAutoCommit(false);
    }

    /**
     * Test EndTransaction.
     * @throws SQLException erreur de transaction.
     */
    @Test
    public void testEndTransactionCommitSQLException() throws SQLException {
        Mockito.doThrow(new SQLException()).when(connection).commit();
        try {
            Transaction.endTransaction(connection, true);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(Transaction.MESS_TRANSACTION_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(connection).commit();
    }

    /**
     * Test EndTransaction.
     * @throws SQLException erreur de transaction.
     */
    @Test
    public void testEndTransactionRollBackSQLException() throws SQLException {
        Mockito.doThrow(new SQLException()).when(connection).rollback();
        try {
            Transaction.endTransaction(connection, false);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(Transaction.MESS_TRANSACTION_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(connection, Mockito.atMost(2)).rollback();
    }

    /**
     * Test EndTransaction.
     * @throws SQLException erreur de transaction.
     */
    @Test
    public void testEndTransactionCloseSQLException() throws SQLException {
        Mockito.doThrow(new SQLException()).when(connection).close();
        try {
            Transaction.endTransaction(connection, true);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(Transaction.MESS_TRANSACTION_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(connection).close();
    }

    /**
     * Test EndTransaction.
     * @throws SQLException erreur de transaction.
     */
    @Test
    public void testEndTransactionSetCommitSQLException() throws SQLException {
        Mockito.doThrow(new SQLException()).when(connection).setAutoCommit(true);
        try {
            Transaction.endTransaction(connection, true);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(Transaction.MESS_TRANSACTION_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(connection).setAutoCommit(true);
    }
}
