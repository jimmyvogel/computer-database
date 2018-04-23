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
import com.excilys.cdb.persistence.exceptions.DaoException;

public class CompanyDaoTest {
	
	private CompanyDao dao;
	private Company companyValid;
	
    @Before
    public void initialisation() throws DAOConfigurationException, DaoException {
    	dao = (CompanyDao) DaoFactory.getInstance().getDao(DaoType.COMPANY);
    	companyValid = dao.getPage(1).getObjects().get(0);
    }

	@Test
	public void testGetAll() throws DaoException {
		long count = dao.getCount();
		Assert.assertTrue(dao.getAll().size()==count);
	}

	@Test
	public void testGetById() throws DaoException {
		Assert.assertTrue(dao.getById(companyValid.getId())!=null);
		Assert.assertFalse(dao.getById(-1)!=null);
	}
	
	@Test
	public void testGetPage() throws DaoException {
		Page<Company> page = dao.getPage(1);
		Assert.assertTrue(page.getObjects().size()==page.getLimit());
	}

}
