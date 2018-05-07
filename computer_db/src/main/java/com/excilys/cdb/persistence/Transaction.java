package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.persistence.exceptions.DaoException;

public class Transaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

    /**
     * Manage the start of a transaction.
     * @param c the connection.
     * @throws DaoException erreur de connection.
     */
    public static void beginTransaction(Connection c) throws DaoException {
        try {
            c.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Connection auto-commit fail", e);
        }
    }

    /**
     * Manage the end of a transaction.
     * @param c the connection.
     * @param resultatOk l'exécution de la requête à fonctionné?
     * @throws DaoException erreur de rollback.
     */
    public static void endTransaction(Connection c, boolean resultatOk) throws DaoException {
        try {
            if (resultatOk) {
                c.commit();
            } else {
                LOGGER.info("rollback transaction");
                c.rollback();
            }
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                LOGGER.error("Rollback fail.");
                throw new DaoException("Requête fail.", e);
            }
            throw new DaoException("Requête exception", e);
        } finally {
            try {
                c.setAutoCommit(true);
                c.close();
            } catch (SQLException e) {
                LOGGER.error("Connection fail.");
                throw new DaoException("Requête fail.", e);
            }
        }
    }

}
