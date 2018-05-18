package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


@Configuration
@ComponentScan(basePackages = {"com.excilys.cdb"})
public class ApplicationSpringConfig implements WebApplicationInitializer {

	private static final AbstractApplicationContext CONTEXT = new AnnotationConfigApplicationContext(ApplicationSpringConfig.class);
	/**
	 */
	public ApplicationSpringConfig() { }
	/**
	 * Get Application context.
	 * @return app context
	 */
	public ApplicationContext getAppContext() {
		return CONTEXT;
	}
	/**
	 * try to close instance, after waiting for a new client for a certain time.
	 */
	public void disuseContext() {
		((ConfigurableApplicationContext) CONTEXT).close();
	}
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		Logger log =  LoggerFactory.getLogger(ApplicationSpringConfig.class);
		log.info("Startup of the application spring context");
	     AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
	     context.register(ApplicationSpringConfig.class);
	     ContextLoaderListener contextLoaderListener = new ContextLoaderListener(context);
	     servletContext.addListener(contextLoaderListener);
	}
}
