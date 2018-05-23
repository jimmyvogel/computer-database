package com.excilys.cdb.webconfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

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
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.servlet", "com.excilys.cdb.ui", "com.excilys.cdb.persistence",
		"com.excilys.cdb.service", "com.excilys.cdb.webconfig" })
public class ApplicationSpringConfig implements WebApplicationInitializer {

	// Root ApplicationContext if no web server launch.
	private static final AbstractApplicationContext CONTEXT = new AnnotationConfigApplicationContext(
			ApplicationSpringConfig.class);

	/**
	 */
	public ApplicationSpringConfig() {
	}

	/**
	 * Get Application context.
	 * @return app context
	 */
	public ApplicationContext getAppContext() {
		return CONTEXT;
	}

	/**
	 */
	public void disuseContext() {
		((ConfigurableApplicationContext) CONTEXT).close();
	}

	// Web Application context
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		Logger log = LoggerFactory.getLogger(ApplicationSpringConfig.class);
		log.info("Startup of the application spring context");
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(ApplicationSpringConfig.class);
		ContextLoaderListener contextLoaderListener = new ContextLoaderListener(context);
		servletContext.addListener(contextLoaderListener);

		// Create and register the DispatcherServlet
		DispatcherServlet servlet = new DispatcherServlet(context);
		ServletRegistration.Dynamic registration = servletContext.addServlet("computer_db", servlet);
		registration.setLoadOnStartup(1);
		registration.addMapping("/");
	}
}
