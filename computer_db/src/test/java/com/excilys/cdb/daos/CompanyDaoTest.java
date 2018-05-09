package com.excilys.cdb.daos;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.DaoFactory.DaoType;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;

public class CompanyDaoTest {

    private CompanyDao dao;
    private Company companyValid;

    /**
     * Initialisation.
     * @throws DAOConfigurationException erreur de configuration
     * @throws DaoException erreur de requête
     */
    @Before
    public void initialisation()
            throws DAOConfigurationException, DaoException {
        dao = (CompanyDao) DaoFactory.getInstance().getDao(DaoType.COMPANY);
        companyValid = dao.getPage(1).getObjects().get(0);
    }

    /**
     * Test sur la méthode getAll.
     * @throws DaoException erreur de requête
     */
    @Test
    public void testGetAll() throws DaoException {
        long count = dao.getCount();
        Assert.assertTrue(dao.getAll().size() == count);
    }

    /**
     * Test de getByName.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testGetByName() throws DaoException {
        String nameTest = "nameValidTestCompanyGetByName";
        long count = dao.getCount();
        long id = dao.create(new Company(nameTest));
        long id2 = dao.create(new Company(nameTest));
        long id3 = dao.create(new Company(nameTest));
        Assert.assertTrue(count + 3 == dao.getCount());
        Assert.assertTrue(dao.getByName(nameTest).size() == 3);
        //Suppression
        List<Long> list = Arrays.asList(id, id2, id3);
        Assert.assertTrue(dao.delete(new HashSet<Long>(list)));
    }

    /**
     * Test sur la méthode getById.
     * @throws DaoException erreur de requête
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetById() throws DaoException {
        Assert.assertTrue(dao.getById(companyValid.getId()) != null);
        dao.getById(-1).get();
    }

    /**
     * Test sur la méthode getPage.
     * @throws DaoException erreur de requête
     */
    @Test(expected = DaoException.class)
    public void testGetPage() throws DaoException {
        Page<Company> page = dao.getPage(1);
        Assert.assertTrue(page.getObjects().size() == page.getLimit());
        dao.getPage(-1);
    }

    /**
     * Test sur la méthode create.
     * @throws DaoException erreur de requête
     */
    @Test
    public void testCreateOk() throws DaoException {
        long count = dao.getCount();
        dao.create(new Company("nomValid"));
        Assert.assertTrue(count + 1 == dao.getCount());
    }

    /**
     * Test sur la méthode create.
     * @throws DaoException erreur de requête
     */
    @Test(expected = DaoException.class)
    public void testCreateNameNull() throws DaoException {
        dao.create(new Company(null));
    }

    /**
     * Test sur la méthode delete.
     * @throws DaoException erreur de requête
     */
    @Test
    public void testDeleteOneElement() throws DaoException {
        long count = dao.getCount();
        long id = dao.create(new Company("nameValid"));
        Assert.assertTrue(count + 1 == dao.getCount());
        Assert.assertTrue(dao.delete(id));
    }

    /**
     * Test sur la méthode delete.
     * @throws DaoException erreur de requête
     */
    @Test
    public void testDeleteManyElement() throws DaoException {
        long count = dao.getCount();
        long id = dao.create(new Company("nameValid"));
        long id2 = dao.create(new Company("nameValid2"));
        long id3 = dao.create(new Company("nameValid3"));
        Assert.assertTrue(count + 3 == dao.getCount());
        List<Long> list = Arrays.asList(id, id2, id3);
        Assert.assertTrue(dao.delete(new HashSet<Long>(list)));
    }

}
