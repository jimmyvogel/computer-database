package com.excilys.cdb.webservices.exceptions;

public class WebServiceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7856970289356950012L;
	
	public WebServiceException() {
		super();
	}
	public WebServiceException(String s) {
		super(s);
	}
	public WebServiceException(Exception e) {
		super(e.getMessage());
	}

}
