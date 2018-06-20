package com.excilys.cdb.testpersistence;

import static org.junit.Assert.assertNotNull;

import javax.sql.DataSource;

import org.junit.Test;

import com.excilys.cdb.persistence.CDBDataSource;

public class CDBDataSourceTest {

	@Test
	public void test() {
		DataSource ds = CDBDataSource.dataSource();
		assertNotNull(ds);
	}

}
