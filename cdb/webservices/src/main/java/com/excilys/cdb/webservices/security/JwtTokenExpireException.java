package com.excilys.cdb.webservices.security;

public class JwtTokenExpireException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723733266672741845L;

	public JwtTokenExpireException() {
		super("The token is expired");
				//MessageHandler.getMessage(ServiceMessage.SPECIAL_CHARACTERS, null));
	}
}
