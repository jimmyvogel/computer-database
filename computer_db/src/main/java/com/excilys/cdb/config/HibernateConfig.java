package com.excilys.cdb.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class HibernateConfig {

	private static final String MODEL_PACKAGE = "com.excilys.cdb.model";

	private static final String HIBERNATE_AUTO_PROPERTY = "hibernate.hbm2ddl.auto";
	private static final String HIBERNATE_AUTO_VALUE = "validate";
	private static final String HIBERNATE_DIALECT_PROPERTY = "hibernate.dialect";
	private static final String HIBERNATE_DIALECT_VALUE = "org.hibernate.dialect.MySQL5Dialect";

	/**
	 * Hibernate Jpa Spring.
	 * @return -
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(CDBDataSource.dataSource());
		em.setPackagesToScan(new String[] {MODEL_PACKAGE });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());

		return em;
	}

	/**
	 * Hibernate Jpa Spring.
	 * @param emf -
	 * @return -
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	/**
	 * Hibernate Jpa Spring.
	 * @return -
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * Hibernate Jpa Spring.
	 * @return -
	 */
	private Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty(HIBERNATE_AUTO_PROPERTY, HIBERNATE_AUTO_VALUE);
		properties.setProperty(HIBERNATE_DIALECT_PROPERTY, HIBERNATE_DIALECT_VALUE);

		return properties;
	}
}
