package com.excilys.cdb.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Class de configuration de spring pour une utilisation sans context web.
 * @author vogel
 *
 */
@EnableJpaRepositories(basePackages = "com.excilys.cdb.persistence")
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.excilys.cdb.persistence", "com.excilys.cdb.service", "com.excilys.cdb.servlet",
		"com.excilys.cdb.exception" })
public class AppSpringConfig {

	private static final String FICHIER_PROPERTIES = "dao.properties";
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_NOM_UTILISATEUR = "username";
	private static final String PROPERTY_MOT_DE_PASSE = "password";

	private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";

	/**
	 * Initialisation de hikaridatasource.
	 * @throws DaoConfigurationException erreur de configuration.
	 * @return datasource singleton.
	 */
	@Bean
	public DataSource dataSource() throws DaoConfigurationException {
		Logger logger = LoggerFactory.getLogger(AppSpringConfig.class);
		logger.info("Initialisation du singleton dao factory");

		HikariConfig config = new HikariConfig();
		HikariDataSource ds;
		try {
			Class.forName(DRIVER_CLASS_NAME);
		} catch (ClassNotFoundException e) {
			throw new DaoConfigurationException(ExceptionHandler.getMessage(MessageException.BDD_CONFIG_DRIVER, null));
		}

		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);
		if (fichierProperties == null) {
			throw new DaoConfigurationException(ExceptionHandler.getMessage(MessageException.BDD_CONFIG_FILE_NOT_FOUND,
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
					ExceptionHandler.getMessage(MessageException.BDD_CONFIG_FILE_FAIL, null), e);
		}
		return ds;
	}
	
	/**
	 * Hibernate Jpa Spring.
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] {"com.excilys.cdb.model" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	/**
	 * Hibernate Jpa Spring.
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	/**
	 * Hibernate Jpa Spring.
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * Hibernate Jpa Spring.
	 */
	private Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "validate");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

		return properties;
	}
}
