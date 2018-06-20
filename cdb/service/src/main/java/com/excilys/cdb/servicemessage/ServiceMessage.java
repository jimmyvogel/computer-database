package com.excilys.cdb.servicemessage;

import com.excilys.cdb.messagehandler.CDBMessage;

public enum ServiceMessage implements CDBMessage{
	
	COMPANY_NOT_FOUND("exception.company.notfound"),
	COMPUTER_NOT_FOUND("exception.company.notfound"),
	SPECIAL_CHARACTERS("exception.security.text.special.characters"),
	ILLEGAL_ARGUMENTS("exception.method.illegal.arguments"),
	COMPUTER_DISCONTINUED_ALONE("exception.computer.validator.discontinued.alone"),
	COMPUTER_INTRODUCED_AFTER("exception.computer.validator.introduced.after"),
	VALIDATION_NAME_NULL("exception.validator.name.null"),
	VALIDATION_NAME_LENGTH("exception.validator.name.length"),
	VALIDATION_DATE_INTRODUCED("exception.validator.date.introduced"),
	VALIDATION_DATE_DISCONTINUED("exception.validator.date.discontinued");

	/** Constructor.
	 * @param v valeur key dans les fichiers de messages.
	 */
	ServiceMessage(String v) {
		this.key = v;
	}
	private String key;
	public String getKey() {
		return key;
	}
	
	public static final String MESSAGE_CLASSPATH_SERVICE = "classpath:strings_service";
	
}