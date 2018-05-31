package com.excilys.cdb.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;

/**
 * Class de configuration de spring pour une utilisation sans context web.
 * @author vogel
 *
 */
@EnableJpaRepositories(basePackages = "com.excilys.cdb.persistence")
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.excilys.cdb.persistence", "com.excilys.cdb.service", "com.excilys.cdb.servlet" })
@ComponentScan(
	    basePackages = {"com.excilys.cdb.config" },
	    useDefaultFilters = false,
	    includeFilters = {
	        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = HibernateConfig.class)
	    })
public class SpringConfigApplication {

	/**
	 * Initialisation de hikaridatasource.
	 * @throws DaoConfigurationException erreur de configuration.
	 * @return datasource singleton.
	 */
	@Bean
	public DataSource dataSource() throws DaoConfigurationException {
		return CDBDataSource.dataSource();
	}

}
