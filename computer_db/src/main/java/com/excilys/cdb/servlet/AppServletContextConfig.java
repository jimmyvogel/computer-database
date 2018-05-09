package com.excilys.cdb.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.servlet.ComputerServlet.Action;

public class AppServletContextConfig implements ServletContextListener {

    private static final String ACTIONS = "actions";

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("ServletContextListener destroyed");
    }

    // Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent event) {
        Map<String, String> map = new HashMap<>();
        for (Action action : ComputerServlet.Action.values()) {
            map.put(action.toString(), action.toString().toLowerCase());
        }
        event.getServletContext().setAttribute(ACTIONS, map);

        Logger logger = LoggerFactory.getLogger(AppServletContextConfig.class);
        logger.info("Configuration du contexte effectué.");
    }

}
