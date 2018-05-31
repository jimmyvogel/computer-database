package com.excilys.cdb.config;

import java.util.Locale;

import javax.sql.DataSource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.excilys.cdb.persistence")
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.excilys.cdb.persistence", "com.excilys.cdb.service",
		"com.excilys.cdb.controller" })
@ComponentScan(basePackages = { "com.excilys.cdb.config" }, useDefaultFilters = false, includeFilters = {
		@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = HibernateConfig.class) })
public class SpringConfigWeb implements WebMvcConfigurer {

	private static final String VIEW_PREFIX = "/WEB-INF/view/";
	private static final String VIEW_SUFFIX = ".jsp";

	private static final String MESSAGE_CLASSPATH = "classpath:strings";
	private static final String MESSAGE_ENCODING = "UTF-8";

	private static final String RESSOURCE_CLASSPATH = "/**";
	private static final String RESSOURCE_LOCATION = "/";
	
	/**
	 * Initialisation de hikaridatasource.
	 * @throws DaoConfigurationException erreur de configuration.
	 * @return datasource singleton.
	 */
	@Bean
	public DataSource dataSource() throws DaoConfigurationException {
		return CDBDataSource.dataSource();
	}

	/**
	 * Initialise le matching des vues.
	 * @return Le viewResolver
	 */
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix(VIEW_PREFIX);
		viewResolver.setSuffix(VIEW_SUFFIX);

		return viewResolver;
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESSOURCE_CLASSPATH).addResourceLocations(RESSOURCE_LOCATION);
	}

	/**
	 * Ajout des fichiers de messages.
	 * @return bena de type messagesource
	 */
	@Bean("messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(MESSAGE_CLASSPATH);
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
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}
}
