package com.excilys.cdb.testconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.persistence", "com.excilys.cdb.service" })
public class TestSpringConfig {

	private static final String FICHIER_PROPERTIES = "dao.properties";
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_NOM_UTILISATEUR = "username";
	private static final String PROPERTY_MOT_DE_PASSE = "password";

	/**
	 * Initialisation de hikaridatasource.
	 * @throws DaoConfigurationException erreur de configuration.
	 * @return datasource singleton.
	 */
	@Bean
	public DataSource dataSource() throws DaoConfigurationException {
		Logger logger = LoggerFactory.getLogger(TestSpringConfig.class);
		logger.info("Initialisation du singleton dao factory");

		HikariConfig config = new HikariConfig();
		HikariDataSource ds;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DaoConfigurationException("Driver introuvable");
		}

		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);
		if (fichierProperties == null) {
			throw new DaoConfigurationException("Le fichier properties " + FICHIER_PROPERTIES + " est introuvable.");
		}
		try {
			properties.load(fichierProperties);
			config.setJdbcUrl(properties.getProperty(PROPERTY_URL));
			config.setUsername(properties.getProperty(PROPERTY_NOM_UTILISATEUR));
			config.setPassword(properties.getProperty(PROPERTY_MOT_DE_PASSE));
			ds = new HikariDataSource(config);
		} catch (IOException e) {
			throw new DaoConfigurationException("Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e);
		}
		return ds;
	}
}
