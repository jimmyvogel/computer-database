package com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class CDBDataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CDBDataSource.class);
	private static final String LOG_DRIVER_NOT_FOUND = "Driver data source fail.";
	private static final String LOG_FILE_PROPERTIES_NOT_FOUND = "File properties for data source not found.";
	private static final String LOG_FILE_PROPERTIES_FAIL = "Loading of file properties for data source fail.";

	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_NOM_UTILISATEUR = "username";
	private static final String PROPERTY_MOT_DE_PASSE = "password";
	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private static final String FICHIER_PROPERTIES = "dao.properties";

	public static DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		HikariDataSource ds = null;
		try {
			Class.forName(DRIVER_CLASS_NAME);
		} catch (ClassNotFoundException e) {
			LOGGER.error(LOG_DRIVER_NOT_FOUND);
		}

		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);
		if (fichierProperties == null) {
			LOGGER.error(LOG_FILE_PROPERTIES_NOT_FOUND);
		}
		try {
			properties.load(fichierProperties);
			config.setJdbcUrl(properties.getProperty(PROPERTY_URL));
			config.setUsername(properties.getProperty(PROPERTY_NOM_UTILISATEUR));
			config.setPassword(properties.getProperty(PROPERTY_MOT_DE_PASSE));
			ds = new HikariDataSource(config);
		} catch (IOException e) {
			LOGGER.error(LOG_FILE_PROPERTIES_FAIL);
		}
		return ds;
	}

}
