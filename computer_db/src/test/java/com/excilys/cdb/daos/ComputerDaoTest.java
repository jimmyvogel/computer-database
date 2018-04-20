package com.excilys.cdb.daos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.DaoFactory.DaoType;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;

public class ComputerDaoTest {

	private ComputerDao dao;
	private Computer computerValid;
	
    @Before
    public void initialisation() throws DAOConfigurationException {
    	dao = (ComputerDao) DaoFactory.getInstance().getDao(DaoType.COMPUTER);
    	computerValid = dao.getPage(1).getObjects().get(0);
    }

	@Test
	public void testGetAll() {
		long count = dao.getCount();
		Assert.assertTrue(dao.getAll().size()==count);
	}

	@Test
	public void testGetById() {
		Assert.assertTrue(dao.getById(computerValid.getId())!=null);
		Assert.assertFalse(dao.getById(-1)!=null);
	}

	@Test
	public void testCreate() {
		long id = dao.create(computerValid);
		Assert.assertTrue(dao.getById(id)!=null);
	}

	@Test
	public void testUpdate() {
		computerValid.setName("nouveau");
		Assert.assertTrue(dao.update(computerValid));
	}

	@Test
	public void testDeleteComputer() {
		long id = dao.create(computerValid);
		Assert.assertTrue(dao.deleteComputer(id));
	}
	
	@Test
	public void testGetPage() {
		Page<Computer> page = dao.getPage(1);
		Assert.assertTrue(page.getObjects().size()==page.getLimit());
	}

}
