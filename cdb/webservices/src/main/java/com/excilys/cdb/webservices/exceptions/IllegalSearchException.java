package com.excilys.cdb.webservices.exceptions;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.webservices.message.WebServiceMessage;

public class IllegalSearchException extends WebServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3074082872977533378L;

	public IllegalSearchException() {
		super(MessageHandler.getMessage(WebServiceMessage.ILLEGAL_SEARCH, null));
	}

}
