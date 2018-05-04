package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.DaoFactory.DaoType;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;

@RunWith(MockitoJUnitRunner.class)
public class ComputerDaoTest {

    @Mock
    private DaoFactory daoFactory;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private Statement mockS;
    @Mock
    private PreparedStatement mockPS;
    @Mock
    private Connection connection;
    @InjectMocks
    private ComputerDao mockDao;

    private ComputerDao dao;
    private Computer computerValid;
    private Company companyValid;

    private final LocalDateTime DATE_INTRODUCED_VALID = LocalDateTime.of(1986,
            Month.APRIL, 8, 12, 30);

    private final LocalDateTime DATE_DISCONTINUED_VALID = LocalDateTime.of(2000,
            Month.APRIL, 8, 12, 30);

    /**
     * Initialisation.
     * @throws DAOConfigurationException erreur de configuration.
     * @throws DaoException erreur de requête.
     */
    @Before
    public void initialisation()
            throws DAOConfigurationException, DaoException {
        MockitoAnnotations.initMocks(this);

        dao = (ComputerDao) DaoFactory.getInstance().getDao(DaoType.COMPUTER);
        computerValid = dao.getPage(1).getObjects().get(0);
        CompanyDao daoCompany = (CompanyDao) DaoFactory.getInstance().getDao(DaoType.COMPANY);
        companyValid = daoCompany.getPage(1).getObjects().get(0);
        computerValid.setCompany(companyValid);
        computerValid.setIntroduced(DATE_INTRODUCED_VALID);
        computerValid.setDiscontinued(DATE_DISCONTINUED_VALID);
    }

    /**
     * Test de getAll.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testGetAll() throws DaoException {
        long count = dao.getCount();
        Assert.assertTrue(dao.getAll().size() == count);
    }

    /**
     * Test de getAll.
     * @throws DaoException erreur de requête.
     * @throws SQLException sql exception
     */
    @Test(expected = DaoException.class)
    public void testGetAllRequeteSQLException() throws DaoException, SQLException {
        Mockito.when(daoFactory.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(mockS);
        Mockito.when(mockS.executeQuery(Mockito.anyString())).thenThrow(new SQLException());
        mockDao.getAll();
        Mockito.verify(daoFactory.getConnection());
        Mockito.verify(connection.createStatement());
        Mockito.verify(mockS).executeQuery(Mockito.anyString());
    }

    /**
     * Test de getById.
     * @throws DaoException erreur de requête.
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetById() throws DaoException {
        Assert.assertTrue(dao.getById(computerValid.getId()) != null);
        dao.getById(-1).get();
    }

    /**
     * Test de getById.
     * @throws DaoException erreur de requête.
     * @throws SQLException sql exception
     */
    @Test(expected = DaoException.class)
    public void testGetByIdRequeteSQLException() throws DaoException, SQLException {
        Mockito.when(daoFactory.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(mockPS);
        Mockito.when(mockPS.executeQuery()).thenThrow(new SQLException());
        mockDao.getById(1);
        Mockito.verify(daoFactory.getConnection());
        Mockito.verify(connection.prepareStatement(Mockito.anyString()));
        Mockito.verify(mockPS.executeQuery());
    }

    /**
     * Test de create.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testCreate() throws DaoException {
        long id = dao.create(computerValid);
        Assert.assertTrue(dao.getById(id) != null);
    }

    /**
     * Test de create.
     * @throws DaoException erreur de requête.
     * @throws SQLException sql exception
     */
    @Test(expected = DaoException.class)
    public void testCreateRequeteSQLException() throws DaoException, SQLException {
        Mockito.when(daoFactory.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt())).thenReturn(mockPS);
        Mockito.when(mockPS.execute()).thenThrow(new SQLException());
        mockDao.create(computerValid);
        Mockito.verify(daoFactory.getConnection());
        Mockito.verify(connection.prepareStatement(Mockito.anyString(), Mockito.anyInt()));
        Mockito.verify(mockPS.execute());
    }

    /**
     * Test de update.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testUpdate() throws DaoException {
        computerValid.setName("nouveau");
        Assert.assertTrue(dao.update(computerValid));
    }

    /**
     * Test de update.
     * @throws DaoException erreur de requête.
     * @throws SQLException sql exception
     */
    @Test(expected = DaoException.class)
    public void testUpdateRequeteSQLException() throws DaoException, SQLException {
        Mockito.when(daoFactory.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(mockPS);
        Mockito.when(mockPS.executeUpdate()).thenThrow(new SQLException());
        mockDao.update(computerValid);
        Mockito.verify(daoFactory.getConnection());
        Mockito.verify(connection.prepareStatement(Mockito.anyString()));
        Mockito.verify(mockPS.executeUpdate());
    }

    /**
     * Test de update.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testUpdateCompanyNullDoChange() throws DaoException {
        computerValid.setCompany(null);
        Assert.assertTrue(dao.update(computerValid));
        Assert.assertNull(dao.getById(computerValid.getId()).get().getCompany());
    }

    /**
     * Test de update.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testUpdateCompanyNotNullDoChange() throws DaoException {
        computerValid.setCompany(companyValid);
        Assert.assertTrue(dao.update(computerValid));
    }

    /**
     * Test de deleteComputer.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testDeleteComputer() throws DaoException {
        long id = dao.create(computerValid);
        Assert.assertTrue(dao.deleteComputer(id));
    }

    /**
     * Test de deleteComputer.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testDeleteComputerFailId() throws DaoException {
        Assert.assertFalse(dao.deleteComputer(Integer.MAX_VALUE));
    }

    /**
     * Test de deleteComputer.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testDeleteComputerFailIdMoinsUn() throws DaoException {
        Assert.assertFalse(dao.deleteComputer(-1));
    }

    /**
     * Test de getPage.
     * @throws DaoException erreur de requête
     */
    @Test(expected = DaoException.class)
    public void testGetPage() throws DaoException {
        Page<Computer> page = dao.getPage(1);
        Assert.assertTrue(page.getObjects().size() == page.getLimit());
        dao.getPage(-1);
    }

}
