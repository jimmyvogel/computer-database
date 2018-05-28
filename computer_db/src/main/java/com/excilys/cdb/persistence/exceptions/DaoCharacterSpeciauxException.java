package com.excilys.cdb.persistence.exceptions;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;

public class DaoCharacterSpeciauxException extends DaoException {

	/**
	 */
	private static final long serialVersionUID = 7732350000952259608L;

	/**
	 * Constructor.
	 */
	public DaoCharacterSpeciauxException() {
		super(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
	}

}
