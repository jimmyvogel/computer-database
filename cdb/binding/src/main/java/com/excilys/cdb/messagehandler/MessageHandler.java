package com.excilys.cdb.messagehandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageHandler{

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
}
