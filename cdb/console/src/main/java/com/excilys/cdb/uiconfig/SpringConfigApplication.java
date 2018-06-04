package com.excilys.cdb.uiconfig;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.excilys.cdb.dao.ComputerCrudDao;
import com.excilys.cdb.persistence.CDBDataSource;
import com.excilys.cdb.persistenceconfig.HibernateConfig;
import com.excilys.cdb.service.ComputerService;

/**
 * Class de configuration de spring pour une utilisation sans context web.
 * @author vogel
 *
 */
@EnableJpaRepositories(basePackageClasses = ComputerCrudDao.class)
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackageClasses = { ComputerCrudDao.class, ComputerService.class, HibernateConfig.class })
public class SpringConfigApplication {

	/**
	 * Initialisation de hikaridatasource.
	 * @throws DaoConfigurationException erreur de configuration.
	 * @return datasource singleton.
	 */
	@Bean
	public DataSource dataSource() {
		return CDBDataSource.dataSource();
	}

}
