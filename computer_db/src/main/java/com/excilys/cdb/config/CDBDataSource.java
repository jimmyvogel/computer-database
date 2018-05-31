package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

import com.excilys.cdb.exception.MessageHandler;
import com.excilys.cdb.exception.MessageHandler.CDBMessage;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class CDBDataSource {

	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_NOM_UTILISATEUR = "username";
	private static final String PROPERTY_MOT_DE_PASSE = "password";
	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private static final String FICHIER_PROPERTIES = "dao.properties";

	public static HikariDataSource dataSource() {
		HikariConfig config = new HikariConfig();
		HikariDataSource ds;
		try {
			Class.forName(DRIVER_CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new DaoConfigurationException(MessageHandler.getMessage(CDBMessage.BDD_CONFIG_DRIVER, null));
		}

		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);
		if (fichierProperties == null) {
			throw new DaoConfigurationException(MessageHandler.getMessage(CDBMessage.BDD_CONFIG_FILE_NOT_FOUND,
					Collections.singleton(FICHIER_PROPERTIES).toArray()));
		}
		try {
			properties.load(fichierProperties);
			config.setJdbcUrl(properties.getProperty(PROPERTY_URL));
			config.setUsername(properties.getProperty(PROPERTY_NOM_UTILISATEUR));
			config.setPassword(properties.getProperty(PROPERTY_MOT_DE_PASSE));
			ds = new HikariDataSource(config);
		} catch (IOException e) {
			throw new DaoConfigurationException(
					MessageHandler.getMessage(CDBMessage.BDD_CONFIG_FILE_FAIL, null), e);
		}
		return ds;
	}

}
