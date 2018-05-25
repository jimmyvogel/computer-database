package com.excilys.cdb.validator.exceptions;

public class ValidatorStringException extends Exception {

    /**
     */
    private static final long serialVersionUID = -9132599808149416148L;

    /**
     * Exception de type string dans la validation.
     * @param s le string non valid.
     * @param reason the reason of the exception.
     */
    public ValidatorStringException(String s, String reason) {
        super(s + " not valid : " + reason);
    }

}
