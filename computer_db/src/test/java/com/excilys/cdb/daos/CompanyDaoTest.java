package com.excilys.cdb.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.config.ApplicationSpringConfig;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.zaxxer.hikari.HikariDataSource;

public class CompanyDaoTest {

    @Mock
    private HikariDataSource dataSource;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private Statement mockS;
    @Mock
    private PreparedStatement mockPS;
    @Mock
    private Connection connection;
    @InjectMocks
    private CompanyDao mockDao;
    private static CompanyDao dao;
    private Company companyValid;
    private static ApplicationSpringConfig appConfig;

    /**
     * Initialisation.
     * @throws DAOConfigurationException
     *             erreur de configuration
     * @throws DaoException
     *             erreur de requête
     */
    @Before
    public void initialisation()
            throws DAOConfigurationException, DaoException {
        MockitoAnnotations.initMocks(this);
        companyValid = dao.getPage(1).getObjects().get(0);
    }

    /**
     * Test sur la méthode getAll.
     * @throws DaoException
     *             erreur de requête
     */
    @Test
    public void testGetAll() throws DaoException {
        long count = dao.getCount();
        Assert.assertTrue(dao.getAll().size() == count);
    }

    /**
     */
    @BeforeClass
    public static void startContext() {
    	appConfig = new ApplicationSpringConfig();
    	dao = (CompanyDao) appConfig.getAppContext().getBean("companyDao");
    }

    /**
     * Test de getAll.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testGetAllRequeteSQLException()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(mockS);
        Mockito.when(mockS.executeQuery(Mockito.anyString()))
                .thenThrow(new SQLException());
        try {
            mockDao.getAll();
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection).createStatement();
        Mockito.verify(mockS).executeQuery(Mockito.anyString());
    }

    /**
     * Test de getByName.
     * @throws DaoException
     *             erreur de requête.
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
        // Suppression
        List<Long> list = Arrays.asList(id, id2, id3);
        Assert.assertTrue(dao.delete(new HashSet<Long>(list)));
    }

    /**
     * Test de getByName.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testGetByNameRequeteSQLException()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(mockPS);
        Mockito.when(mockPS.executeQuery()).thenThrow(new SQLException());
        try {
            mockDao.getByName("name");
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(mockPS).executeQuery();
    }

    /**
     * Test sur la méthode getById.
     * @throws DaoException
     *             erreur de requête
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetById() throws DaoException {
        Assert.assertTrue(dao.getById(companyValid.getId()) != null);
        dao.getById(-1).get();
    }

    /**
     * Test de getById.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testGetByIdRequeteSQLException()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(mockPS);
        Mockito.when(mockPS.executeQuery()).thenThrow(new SQLException());
        try {
            mockDao.getById(1);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection).prepareStatement(Mockito.anyString());
        Mockito.verify(mockPS).executeQuery();
    }

    /**
     * Test sur la méthode getPage.
     * @throws DaoException
     *             erreur de requête
     */
    @Test(expected = DaoException.class)
    public void testGetPage() throws DaoException {
        Page<Company> page = dao.getPage(1);
        Assert.assertTrue(page.getObjects().size() == page.getLimit());
        dao.getPage(-1);
    }

    /**
     * Test de getPageSearch.
     * @throws DaoException erreur de requête
     */
    @Test
    public void testGetPageSearchOk() throws DaoException {
        String search = "TestCompanyGetPageSearch";
        Integer nbElements = 23;
        Set<Long> ids = new HashSet<Long>();
        for (int i = 0; i < nbElements; i++) {
            ids.add(dao.create(new Company(search)));
        }
        Page<Company> page = dao.getPageSearch(search, 1);
        Assert.assertTrue(page.getCount() == nbElements);

        dao.delete(ids);
    }

    /**
     * Test de getPageSearch.
     * @throws DaoException erreur de requête
     */
    @Test(expected = DaoException.class)
    public void testGetPageSearchIndexException() throws DaoException {
        dao.getPageSearch("nomauhasard", -1);
    }

    /**
     * Test sur la méthode create.
     * @throws DaoException
     *             erreur de requête
     */
    @Test
    public void testCreateOk() throws DaoException {
        long count = dao.getCount();
        dao.create(new Company("nomValid"));
        Assert.assertTrue(count + 1 == dao.getCount());
    }

    /**
     * Test de getById.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testCreateRequeteSQLException()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString(),
                Mockito.anyInt())).thenReturn(mockPS);
        Mockito.when(mockPS.execute()).thenThrow(new SQLException());
        try {
            mockDao.create(companyValid);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection).prepareStatement(Mockito.anyString(),
                Mockito.anyInt());
        Mockito.verify(mockPS).execute();
    }

    /**
     * Test sur la méthode create.
     * @throws DaoException
     *             erreur de requête
     */
    @Test(expected = DaoException.class)
    public void testCreateNameNull() throws DaoException {
        dao.create(new Company(null));
    }

    /**
     * Test sur la méthode delete.
     * @throws DaoException
     *             erreur de requête
     */
    @Test
    public void testDeleteOneElement() throws DaoException {
        long count = dao.getCount();
        long id = dao.create(new Company("nameValid"));
        Assert.assertTrue(count + 1 == dao.getCount());
        Assert.assertTrue(dao.delete(id));
    }

    /**
     * Test de delete.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testDeleteRequeteSQLException()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(mockPS);
        Mockito.when(mockPS.executeUpdate()).thenThrow(new SQLException());
        try {
            mockDao.delete(1);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection, Mockito.atMost(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(mockPS, Mockito.atMost(2)).executeUpdate();
    }

    /**
     * Test de delete.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testDeleteRequeteSQLExceptionWhenRollback()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(mockPS);
        Mockito.when(mockPS.executeUpdate()).thenThrow(new SQLException());
        Mockito.doThrow(new SQLException()).when(connection).rollback();
        try {
            mockDao.delete(1);
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(connection).rollback();
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection, Mockito.atMost(2)).prepareStatement(Mockito.anyString());
        Mockito.verify(mockPS, Mockito.atMost(2)).executeUpdate();
    }

    /**
     * Test de delete.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testDeleteIdAbsent() throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.prepareStatement(Mockito.anyString()))
                .thenReturn(mockPS);
        Mockito.when(mockPS.executeUpdate()).thenReturn(-1);
        Assert.assertFalse(mockDao.delete(1));
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection, Mockito.atMost(2))
                .prepareStatement(Mockito.anyString());
        Mockito.verify(mockPS, Mockito.atMost(2)).executeUpdate();
    }

    /**
     * Test sur la méthode delete.
     * @throws DaoException
     *             erreur de requête
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

    /**
     * Test de getCount.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testCountNextFalseReturnZero()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(mockS);
        Mockito.when(mockS.executeQuery(Mockito.anyString()))
                .thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);
        Assert.assertEquals(mockDao.getCount(), 0);
        Mockito.verify(mockResultSet, Mockito.atLeastOnce()).next();
        Mockito.verify(dataSource, Mockito.atLeastOnce()).getConnection();
        Mockito.verify(connection, Mockito.atLeastOnce()).createStatement();
        Mockito.verify(mockS).executeQuery(Mockito.anyString());
    }

    /**
     * Test de getAll.
     * @throws DaoException
     *             erreur de requête.
     * @throws SQLException
     *             sql exception
     */
    @Test
    public void testCountRequeteSQLException()
            throws DaoException, SQLException {
        Mockito.when(dataSource.getConnection()).thenReturn(connection);
        Mockito.when(connection.createStatement()).thenReturn(mockS);
        Mockito.when(mockS.executeQuery(Mockito.anyString()))
                .thenThrow(new SQLException());
        try {
            mockDao.getCount();
            Assert.fail();
        } catch (DaoException expected) {
            Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
        }
        Mockito.verify(dataSource).getConnection();
        Mockito.verify(connection).createStatement();
        Mockito.verify(mockS).executeQuery(Mockito.anyString());
    }
}
