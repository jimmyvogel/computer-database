package com.excilys.cdb.persistence.exceptions;

public class DaoConfigurationException extends RuntimeException {

    /**
     */
    private static final long serialVersionUID = -3740823104873709188L;

    /**
     * Constructor de l'exception.
     * @param string le problème
     */
    public DaoConfigurationException(String string) {
        super(string);
    }

    /**
     * Constructor de l'exception.
     * @param string le problème
     * @param e l'exception précédente
     */
    public DaoConfigurationException(String string, Exception e) {
        super(string, e);
    }

}
