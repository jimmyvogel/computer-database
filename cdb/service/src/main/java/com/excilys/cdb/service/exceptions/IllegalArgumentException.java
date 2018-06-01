package com.excilys.cdb.service.exceptions;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.servicemessage.ServiceMessage;

public class IllegalArgumentException extends ServiceException {

	/**
	 */
	private static final long serialVersionUID = 8109378523318975587L;

	/**
	 * Constructor.
	 */
	public IllegalArgumentException() {
		super(MessageHandler.getMessage(ServiceMessage.ILLEGAL_ARGUMENTS, null));
	}

}
