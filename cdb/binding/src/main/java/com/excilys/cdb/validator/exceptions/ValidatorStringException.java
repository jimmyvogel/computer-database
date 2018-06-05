package com.excilys.cdb.validator.exceptions;

public class ValidatorStringException extends Exception {

	public enum TypeError{
		ILLEGAL_CHARACTERS,
		NULL_STRING,
		BAD_LENGTH;
	}
	
	private TypeError type;
	
    /**
     */
    private static final long serialVersionUID = -9132599808149416148L;

    /**
     * Exception de type string dans la validation.
     * @param reason the reason of the exception.
     */
    public ValidatorStringException(String reason, TypeError error) {
        super(reason);
        this.type = error;
    }
    
    public ValidatorStringException(TypeError error) { 
    	super();
    	this.type = error;
    }
    
    public TypeError getTypeError() {
    	return type;
    }

}
