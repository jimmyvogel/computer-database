package com.excilys.cdb.validator.exceptions;

public class ValidatorStringException extends Exception {

    /**
     */
    private static final long serialVersionUID = -9132599808149416148L;

    /**
     * Exception de type string dans la validation.
     * @param reason the reason of the exception.
     */
    public ValidatorStringException(String reason) {
        super(reason);
    }
    
    public ValidatorStringException() { 
    	super();
    }

}
