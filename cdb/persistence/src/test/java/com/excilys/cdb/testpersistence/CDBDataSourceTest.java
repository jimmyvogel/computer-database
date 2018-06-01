package com.excilys.cdb.testpersistence;

import static org.junit.Assert.*;

import org.junit.Test;

import com.excilys.cdb.persistence.CDBDataSource;
import com.zaxxer.hikari.HikariDataSource;

public class CDBDataSourceTest {

	@Test
	public void test() {
		HikariDataSource ds = CDBDataSource.dataSource();
		assertNotNull(ds);
	}

}
