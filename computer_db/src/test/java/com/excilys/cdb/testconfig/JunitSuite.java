package com.excilys.cdb.testconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.excilys.cdb.exception.ExceptionHandler;

public class JunitSuite {

	protected static AbstractApplicationContext context = new AnnotationConfigApplicationContext(
			TestSpringConfig.class);

	/**
	 */
	public JunitSuite() {
		ExceptionHandler.init(context);
	}
}
