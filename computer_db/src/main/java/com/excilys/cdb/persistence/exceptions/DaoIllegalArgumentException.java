package com.excilys.cdb.persistence.exceptions;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;

public class DaoIllegalArgumentException extends DaoException {

	/**
	 */
	private static final long serialVersionUID = 8109378523318975587L;

	/**
	 * Constructor.
	 */
	public DaoIllegalArgumentException() {
		super(ExceptionHandler.getMessage(MessageException.ILLEGAL_ARGUMENTS, null));
	}

}
