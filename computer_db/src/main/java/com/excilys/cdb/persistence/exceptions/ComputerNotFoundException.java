package com.excilys.cdb.persistence.exceptions;

public class ComputerNotFoundException extends DaoException {

    /**
     */
    private static final long serialVersionUID = -8927718135567241161L;

    /**
     * Exception throw when a computer is not found in the bdd.
     * @param id the id specified.
     */
    public ComputerNotFoundException(final long id) {
        super("Computer id (" + id + ") is not valid");
    }

}
