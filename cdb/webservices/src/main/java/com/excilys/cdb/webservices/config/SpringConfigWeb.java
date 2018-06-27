package com.excilys.cdb.webservices.config;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.excilys.cdb.dao.ComputerCrudDao;
import com.excilys.cdb.persistence.CDBDataSource;
import com.excilys.cdb.persistenceconfig.HibernateConfig;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.servicemessage.ServiceMessage;
import com.excilys.cdb.webservices.ComputerWebService;
import com.excilys.cdb.webservices.jacksonconfig.JacksonConfig;
import com.excilys.cdb.webservices.message.WebServiceMessage;
import com.excilys.cdb.webservices.security.JwtTokenUtil;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackageClasses = ComputerCrudDao.class)
@EnableTransactionManagement
@ComponentScan(basePackageClasses = { ComputerCrudDao.class, ComputerService.class, ComputerWebService.class,
		HibernateConfig.class, JacksonConfig.class, JwtTokenUtil.class })
public class SpringConfigWeb implements WebMvcConfigurer {

	private static final String MESSAGE_ENCODING = "UTF-8";

	/**
	 * Initialisation de hikaridatasource.
	 * @throws DaoConfigurationException erreur de configuration.
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
		messageSource.setBasenames(ServiceMessage.MESSAGE_CLASSPATH_SERVICE, WebServiceMessage.MESSAGE_CLASSPATH_CONTROLLER);
		messageSource.setDefaultEncoding(MESSAGE_ENCODING);
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	/**
	 * Retourne le cookielocale resolver défaut.
	 * @return bean de type localeResolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(Locale.FRENCH);
		return localeResolver;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("OPTIONS", "GET", "PUT", "POST", "DELETE")
			.allowCredentials(true).maxAge(3600);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}
}
