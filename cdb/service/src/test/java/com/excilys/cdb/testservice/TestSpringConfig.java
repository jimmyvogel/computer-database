package com.excilys.cdb.testservice;

import javax.sql.DataSource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.excilys.cdb.dao.CompanyCrudDao;
import com.excilys.cdb.persistence.CDBDataSource;
import com.excilys.cdb.persistenceconfig.HibernateConfig;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.servicemessage.ServiceMessage;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = CompanyCrudDao.class)
@ComponentScan(basePackageClasses = {CompanyCrudDao.class, CompanyService.class, HibernateConfig.class})
public class TestSpringConfig implements WebMvcConfigurer {

	/**
	 * Initialisation de hikaridatasource.
	 * @return datasource singleton.
	 */
	@Bean
	public DataSource dataSource() {
		return CDBDataSource.dataSource();
	}

	/**
	 * Ajout des fichiers de messages.
	 * @return bena de type messagesource
	 */
	@Bean("messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(ServiceMessage.MESSAGE_CLASSPATH_SERVICE);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}
}
