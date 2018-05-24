package com.excilys.cdb.testconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class TestSuite {

    protected static AbstractApplicationContext context = new AnnotationConfigApplicationContext(TestSpringConfig.class);

}
