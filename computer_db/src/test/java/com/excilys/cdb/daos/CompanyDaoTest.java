package com.excilys.cdb.daos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.DaoFactory.DaoType;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;

public class CompanyDaoTest {
	
	private CompanyDao dao;
	private Company companyValid;
	
    @Before
    public void initialisation() throws DAOConfigurationException {
    	dao = (CompanyDao) DaoFactory.getInstance().getDao(DaoType.COMPANY);
    	companyValid = dao.getPage(1).getObjects().get(0);
    }

	@Test
	public void testGetAll() {
		long count = dao.getCount();
		Assert.assertTrue(dao.getAll().size()==count);
	}

	@Test
	public void testGetById() {
		Assert.assertTrue(dao.getById(companyValid.getId())!=null);
		Assert.assertFalse(dao.getById(-1)!=null);
	}
	
	@Test
	public void testGetPage() {
		Page<Company> page = dao.getPage(1);
		Assert.assertTrue(page.getObjects().size()==page.getLimit());
	}

}
