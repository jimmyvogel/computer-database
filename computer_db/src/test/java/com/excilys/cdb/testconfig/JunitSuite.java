package com.excilys.cdb.testconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;

public class JunitSuite {

	private static final Logger LOGGER = LoggerFactory.getLogger(JunitSuite.class);
	protected static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestSpringConfig.class);
	protected IComputerService serviceComputer;
	protected ICompanyService serviceCompany;
	/**
	 */
	public JunitSuite() {
		ExceptionHandler.init(context);
		serviceComputer = (IComputerService) context.getBean(IComputerService.class);
		serviceCompany = (ICompanyService) context.getBean(ICompanyService.class);
	}
}