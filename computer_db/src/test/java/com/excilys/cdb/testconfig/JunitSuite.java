package com.excilys.cdb.testconfig;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.exception.ExceptionHandler;

@SpringJUnitConfig(classes = TestSpringConfig.class)
public class JunitSuite implements ApplicationContextAware {

	protected static AbstractApplicationContext co = new AnnotationConfigApplicationContext(TestSpringConfig.class);

	@Autowired
	ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext c) throws BeansException {
		this.context = c;
	}

	/**
	 */
	public JunitSuite() {
		setApplicationContext(co);
		ExceptionHandler.init(context);
		System.out.println(context);
	}
}