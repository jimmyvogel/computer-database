package com.excilys.cdb.webservices.message;

import com.excilys.cdb.messagehandler.CDBMessage;

public enum WebServiceMessage implements CDBMessage {
	CONFLIT_EXCEPTION("exception.webservices.conflit"),
	ILLEGAL_SEARCH("exception.webservices.bad.search"), 
	UNCHECK_EXCEPTION("exception.webservices.uncheck"), 
	TYPE_MISMATCH("exception.webservices.type.mismatch");

	/** Constructor.
	 * @param v valeur key dans les fichiers de messages.
	 */
	WebServiceMessage(String v) {
		this.key = v;
	}
	private String key;
	public String getKey() {
		return key;
	}
	
}