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

import com.excilys.cdb.servlet.ressources.Action;

@WebListener
public class WebServletContextConfig implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebServletContextConfig.class);
    private static final String ACTIONS = "actions";

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	LOGGER.info("ServletContextListener destroyed");
    }

    // Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Map<String, String> map = new HashMap<>();
        for (Action action : Action.values()) {
            map.put(action.toString(), action.getValue());
        }
        event.getServletContext().setAttribute(ACTIONS, map);
        event.getServletContext().setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
        LOGGER.info("Configuration du contexte effectué.");
    }

}
