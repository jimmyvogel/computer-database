package com.excilys.cdb.service.exceptions;

import com.excilys.cdb.exception.MessageHandler;
import com.excilys.cdb.exception.MessageHandler.CDBMessage;

public class IllegalArgumentException extends ServiceException {

	/**
	 */
	private static final long serialVersionUID = 8109378523318975587L;

	/**
	 * Constructor.
	 */
	public IllegalArgumentException() {
		super(MessageHandler.getMessage(CDBMessage.ILLEGAL_ARGUMENTS, null));
	}

}
