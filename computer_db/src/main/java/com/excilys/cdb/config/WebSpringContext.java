package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.excilys.cdb.exception.ExceptionHandler;

public class WebSpringContext implements WebApplicationInitializer {

	// Web Application context
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		Logger log = LoggerFactory.getLogger(WebSpringConfig.class);
		log.info("Startup of the application spring context");
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(WebSpringConfig.class);

		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
		ExceptionHandler.init(context);
	}

}
