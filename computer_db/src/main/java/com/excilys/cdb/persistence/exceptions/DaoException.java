package com.excilys.cdb.persistence.exceptions;

public class DaoException extends Exception {

    /**
     */
    private static final long serialVersionUID = -1897494936767738170L;

    /**
     * Constructor de l'exception.
     * @param string le problème.
     */
    public DaoException(String string) {
        super(string);
    }

    /**
     * Constructor de l'exception.
     * @param string le problème
     * @param e l'exception levé précédant celle-ci.
     */
    public DaoException(String string, Exception e) {
        super(string, e);
    }
}
