package com.excilys.cdb.service.exceptions;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5980335044814836732L;

	public ServiceException(String string) {
		super(string);
	}

	public ServiceException(String string, Exception e) {
		super(string, e);
	}
}
