package com.excilys.cdb.validator.exceptions;

public class ValidatorDateException extends Exception {

    /**
     */
    private static final long serialVersionUID = -9132599808149416148L;

    /**
     * Exception de type date dans la validation.
     * @param reason the reason of the exception.
     */
    public ValidatorDateException(String reason) {
        super(reason);
    }
}
