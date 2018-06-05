package com.excilys.cdb.validator.exceptions;

public class ValidatorDateException extends Exception {

	public enum TypeError{
		SECOND_DATE_ALONE,
		ILLEGAL_DATE,
		ILLEGAL_DATE_2,
		SECOND_DATE_AFTER_FAIL;
	}
	
	private TypeError type;
	
    /**
     */
    private static final long serialVersionUID = -9132599808149416148L;

    /**
     * Exception de type date dans la validation.
     * @param reason the reason of the exception.
     */
    public ValidatorDateException(String reason, TypeError error) {
        super(reason);
        this.type = error;
    }
    
    public ValidatorDateException(TypeError error) {
    	super();
    	this.type = error;
    }
    
    public TypeError getTypeError() {
    	return type;
    }
}
