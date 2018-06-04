package com.excilys.cdb.config;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.SessionTrackingMode;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.ressources.Action;

@WebListener
public class WebServletContextConfig implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebServletContextConfig.class);
	private static final String LOGGER_SERVLET_CONTEXT_INITIALIZED = "Configuration du contexte effectué.";
	private static final String LOGGER_SERVLET_CONTEXT_DISABLE = "ServletContextListener destroyed";

	private static final String ACTIONS = "actions";

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		LOGGER.info(LOGGER_SERVLET_CONTEXT_INITIALIZED);
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Map<String, String> map = new HashMap<>();
		for (Action action : Action.values()) {
			map.put(action.toString(), action.getValue());
		}
		event.getServletContext().setAttribute(ACTIONS, map);
		event.getServletContext().setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
		LOGGER.info(LOGGER_SERVLET_CONTEXT_DISABLE);
	}

}
