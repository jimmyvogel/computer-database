package com.excilys.cdb.service.exceptions;

public class NameInvalidException extends ServiceException {

	/**
	 */
	private static final long serialVersionUID = 6765269871660816698L;

	/**
	 * Exception sur le nom d'un objet utilisé par le service.
	 * @param reason la raison de l'erreur
	 */
	public NameInvalidException(String reason) {
		super(reason);
	}

}
