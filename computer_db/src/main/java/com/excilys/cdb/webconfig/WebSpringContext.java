package com.excilys.cdb.webconfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebSpringContext implements WebApplicationInitializer {

	// Web Application context
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		Logger log = LoggerFactory.getLogger(WebSpringConfig.class);
		log.info("Startup of the application spring context");
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(WebSpringConfig.class);
		// ContextLoaderListener contextLoaderListener = new
		// ContextLoaderListener(context);
		// servletContext.addListener(contextLoaderListener);

		// Create and register the DispatcherServlet
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
	}

}
