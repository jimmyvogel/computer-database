package com.excilys.cdb.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageHandler {

	private static ApplicationContext context;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * .
	 * @param c .
	 */
	public static void init(ApplicationContext c) {
		if (context == null) {
			context = c;
		}
	}

	/**
	 * Méthode pour récupérer les messages d'exceptions dans les fichiers ressources.
	 * @param message le type de message d'exception a retourné.
	 * @param params paramètres de messages
	 * @return le string d'exception demandé.
	 */
	public static String getMessage(CDBMessage message, Object[] params) {
		String res = "";
		if (context == null) {
			LOGGER.error("Le context n'a pas été instancié");
		} else {
			res = context.getBean(MessageSource.class).getMessage(message.getKey(), params, LocaleContextHolder.getLocale());
		}
		return res;
	}

	public enum CDBMessage {
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
		CDBMessage(String v) {
			this.key = v;
		}
		private String key;
		public String getKey() {
			return key;
		}
	}
}
