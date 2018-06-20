package com.excilys.cdb.controllermessage;

import com.excilys.cdb.messagehandler.CDBMessage;

public enum ControllerMessage implements CDBMessage{
	ILLEGAL_ARGUMENTS("exception.method.illegal.arguments"),
	DELETE_FAIL("exception.delete.fail"),
	SUCCESS_DELETION("success.deletion"),
	SUCCESS_CREATE("success.create"),
	SUCCESS_UPDATE("success.update");

	/** Constructor.
	 * @param v valeur key dans les fichiers de messages.
	 */
	ControllerMessage(String v) {
		this.key = v;
	}
	private String key;
	public String getKey() {
		return key;
	}
	
	public static final String MESSAGE_CLASSPATH_WEBAPP = "classpath:strings_webapp";
}