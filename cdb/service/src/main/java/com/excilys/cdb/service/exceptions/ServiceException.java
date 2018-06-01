package com.excilys.cdb.service.exceptions;

public class ServiceException extends Exception {

    /**
     */
    private static final long serialVersionUID = 5980335044814836732L;

    /**
     * Constructor de l'exception.
     * @param string le problème
     */
    public ServiceException(String string) {
        super(string);
    }

    /**
     * Constructor de l'exception.
     * @param string le problème
     * @param e l'exception précédente
     */
    public ServiceException(String string, Exception e) {
        super(string, e);
    }
}
