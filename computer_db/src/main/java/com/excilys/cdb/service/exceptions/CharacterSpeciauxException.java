package com.excilys.cdb.service.exceptions;

import com.excilys.cdb.exception.MessageHandler;
import com.excilys.cdb.exception.MessageHandler.CDBMessage;

public class CharacterSpeciauxException extends ServiceException {

	/**
	 */
	private static final long serialVersionUID = 7732350000952259608L;

	/**
	 * Constructor.
	 */
	public CharacterSpeciauxException() {
		super(MessageHandler.getMessage(CDBMessage.SPECIAL_CHARACTERS, null));
	}

}
