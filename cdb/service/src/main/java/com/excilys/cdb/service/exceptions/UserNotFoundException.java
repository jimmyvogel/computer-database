package com.excilys.cdb.service.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.servicemessage.ServiceMessage;

public class UserNotFoundException extends UsernameNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3509582406587692254L;

	public UserNotFoundException() {
		super(MessageHandler.getMessage(ServiceMessage.COMPANY_NOT_FOUND, null));
	}
}
