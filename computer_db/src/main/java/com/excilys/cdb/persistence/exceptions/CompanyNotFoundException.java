package com.excilys.cdb.persistence.exceptions;

import java.util.Collections;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;

public class CompanyNotFoundException extends DaoException {

    /**
     */
    private static final long serialVersionUID = -7956627033406728774L;

    /**
     * Exception throw when a company is not found in the bdd.
     * @param id the id specified.
     */
    public CompanyNotFoundException(long id) {
        super(ExceptionHandler.getMessage(MessageException.COMPANY_NOT_FOUND, Collections.singleton(id).toArray()));
    }

}
