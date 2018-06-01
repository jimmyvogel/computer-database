package com.excilys.cdb.servicemessage;

import com.excilys.cdb.messagehandler.CDBMessage;

public enum ServiceMessage implements CDBMessage{
	COMPANY_NOT_FOUND("exception.company.notfound"),
	COMPUTER_NOT_FOUND("exception.company.notfound"),
	SPECIAL_CHARACTERS("exception.security.text.special.characters"),
	ILLEGAL_ARGUMENTS("exception.method.illegal.arguments"),
	BDD_CONFIG_DRIVER("exception.bdd.config.driver"),
	BDD_CONFIG_FILE_NOT_FOUND("exception.bdd.config.file.notfound"),
	BDD_CONFIG_FILE_FAIL("exception.bdd.config.file.download"),
	COMPUTER_DISCONTINUED_ALONE("exception.computer.validator.discontinued.alone"),
	COMPUTER_INTRODUCED_AFTER("exception.computer.validator.introduced.after"),
	VALIDATION_NAME_NULL("exception.validator.name.null"),
	VALIDATION_NAME_LENGTH("exception.validator.name.length"),
	VALIDATION_DATE_INTRODUCED("exception.validator.date.introduced"),
	VALIDATION_DATE_DISCONTINUED("exception.validator.date.discontinued"),
	DELETE_FAIL("exception.delete.fail"),
	SUCCESS_DELETION("success.deletion"),
	SUCCESS_CREATE("success.create"),
	SUCCESS_UPDATE("success.update");

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
	
}