package com.excilys.cdb.webconfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.excilys.cdb.persistence", "com.excilys.cdb.service", "com.excilys.cdb.servlet" })
public class WebSpringConfig implements WebMvcConfigurer {

	private static final String FICHIER_PROPERTIES = "dao.properties";
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_NOM_UTILISATEUR = "username";
	private static final String PROPERTY_MOT_DE_PASSE = "password";

	/**
	 * Initialisation de hikaridatasource.
	 * @throws DAOConfigurationException erreur de configuration.
	 * @return datasource singleton.
	 */
	@Bean
	public DataSource dataSource() throws DAOConfigurationException {
		Logger logger = LoggerFactory.getLogger(WebSpringConfig.class);
		logger.info("Initialisation du singleton dao factory");

		HikariConfig config = new HikariConfig();
		HikariDataSource ds;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DAOConfigurationException("Driver introuvable");
		}

		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);
		if (fichierProperties == null) {
			throw new DAOConfigurationException("Le fichier properties " + FICHIER_PROPERTIES + " est introuvable.");
		}
		try {
			properties.load(fichierProperties);
			config.setJdbcUrl(properties.getProperty(PROPERTY_URL));
			config.setUsername(properties.getProperty(PROPERTY_NOM_UTILISATEUR));
			config.setPassword(properties.getProperty(PROPERTY_MOT_DE_PASSE));
			ds = new HikariDataSource(config);
		} catch (IOException e) {
			throw new DAOConfigurationException("Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e);
		}
		return ds;
	}

	/**
	 * Initialise le matching des vues.
	 * @return Le viewResolver
	 */
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}
}
