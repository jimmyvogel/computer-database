package com.excilys.cdb.persistence.exceptions;

public class CompanyNotFoundException extends DaoException {

    /**
     */
    private static final long serialVersionUID = -7956627033406728774L;

    /**
     * Exception throw when a company is not found in the bdd.
     * @param id the id specified.
     */
    public CompanyNotFoundException(long id) {
        super("Company id (" + id + ") is not valid");
    }

}
