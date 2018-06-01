package com.excilys.cdb.service.exceptions;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.servicemessage.ServiceMessage;

public class CharacterSpeciauxException extends ServiceException {

	/**
	 */
	private static final long serialVersionUID = 7732350000952259608L;

	/**
	 * Constructor.
	 */
	public CharacterSpeciauxException() {
		super(MessageHandler.getMessage(ServiceMessage.SPECIAL_CHARACTERS, null));
	}

}
