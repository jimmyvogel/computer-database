package com.excilys.cdb.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ApplicationSpringConfig {

	private static final ApplicationContext CONTEXT = new FileSystemXmlApplicationContext(
			"WebContent/WEB-INF/applicationContext.xml");

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
}
