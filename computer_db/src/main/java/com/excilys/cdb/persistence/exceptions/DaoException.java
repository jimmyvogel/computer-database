package com.excilys.cdb.persistence.exceptions;

public class DaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1897494936767738170L;

	public DaoException(String string) {
		super(string);
	}

	public DaoException(String string, Exception e) {
		super(string, e);
	}
}
