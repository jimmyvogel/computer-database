package com.excilys.cdb.testpersistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.Month;
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
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.testconfig.JunitSuite;
import com.zaxxer.hikari.HikariDataSource;

public class ComputerDaoTest extends JunitSuite {

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
    private ComputerDao mockDao;

    private static CompanyDao daoCompany;
    private static ComputerDao dao;
    private Computer computerValid;
    private Company companyValid;

    private final LocalDateTime DATE_INTRODUCED_VALID = LocalDateTime.of(1986,
            Month.APRIL, 8, 12, 30);

    private final LocalDateTime DATE_DISCONTINUED_VALID = LocalDateTime.of(2000,
            Month.APRIL, 8, 12, 30);

//    private static ApplicationSpringConfig appConfig;

    /**
     */
    @BeforeClass
    public static void startContext() {
    	dao = (ComputerDao) context.getBean("computerDao");
    	daoCompany = (CompanyDao) context.getBean("companyDao");
    }

    /**
     * Initialisation.
     * @throws DaoConfigurationException erreur de configuration.
     * @throws DaoException erreur de requête.
     */
    @Before
    public void initialisation()
            throws DaoConfigurationException, DaoException {
        MockitoAnnotations.initMocks(this);

        computerValid = dao.getPage(1).getObjects().get(0);
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
     * Test de getByName.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testGetByName() throws DaoException {
        String nameTest = "nameValidTestGetByNameFESF";
        long count = dao.getCount();
        long id = dao.create(new Computer(nameTest));
        long id2 = dao.create(new Computer(nameTest));
        long id3 = dao.create(new Computer(nameTest));
        Assert.assertTrue(count + 3 == dao.getCount());
        Assert.assertTrue(dao.getByName(nameTest).size() == 3);
        //Suppression
        List<Long> list = Arrays.asList(id, id2, id3);
        Assert.assertTrue(dao.delete(new HashSet<Long>(list)));
    }

//    /**
//     * Test de getByName.
//     * @throws DaoException erreur de requête.
//     * @throws SQLException sql exception
//     */
//    @Test
//    public void testGetByNameRequeteSQLException() throws DaoException, SQLException {
//        Mockito.when(dataSource.getConnection()).thenReturn(connection);
//        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(mockPS);
//        Mockito.when(mockPS.executeQuery()).thenThrow(new SQLException());
//        try {
//            mockDao.getByName("name");
//            Assert.fail();
//        } catch (DaoException expected) {
//            Assert.assertEquals(ComputerDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
//        }
//        Mockito.verify(dataSource).getConnection();
//        Mockito.verify(connection).prepareStatement(Mockito.anyString());
//        Mockito.verify(mockPS).executeQuery();
//    }

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
     * Test de create.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testCreate() throws DaoException {
        long id = dao.create(computerValid);
        Assert.assertTrue(dao.getById(id) != null);
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
        Assert.assertTrue(dao.delete(id));
    }

    /**
     * Test de deleteComputer.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testDeleteComputerFailId() throws DaoException {
        Assert.assertFalse(dao.delete(Integer.MAX_VALUE));
    }

    /**
     * Test de deleteComputer.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testDeleteComputerFailIdMoinsUn() throws DaoException {
        Assert.assertFalse(dao.delete(-1));
    }

    /**
     * Test de deleteComputer.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testDeleteListComputer() throws DaoException {
        long count = dao.getCount();
        long id = dao.create(new Computer("nameValid"));
        long id2 = dao.create(new Computer("nameValid2"));
        long id3 = dao.create(new Computer("nameValid3"));
        Assert.assertTrue(count + 3 == dao.getCount());
        List<Long> list = Arrays.asList(id, id2, id3);
        Assert.assertTrue(dao.delete(new HashSet<Long>(list)));
    }

//    /**
//     * Test de delete.
//     * @throws DaoException erreur de requête.
//     * @throws SQLException sql exception
//     */
//    @Test
//    public void testDeleteRequeteSQLException() throws DaoException, SQLException {
//        Mockito.when(dataSource.getConnection()).thenReturn(connection);
//        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(mockPS);
//        Mockito.when(mockPS.executeUpdate()).thenThrow(new SQLException());
//        try {
//            mockDao.delete(1);
//            Assert.fail();
//        } catch (DaoException expected) {
//            Assert.assertEquals(ComputerDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
//        }
//        Mockito.verify(dataSource).getConnection();
//        Mockito.verify(connection).prepareStatement(Mockito.anyString());
//        Mockito.verify(mockPS).executeUpdate();
//    }

//    /**
//     * Test de delete.
//     * @throws DaoException erreur de requête.
//     * @throws SQLException sql exception
//     */
//    @Test
//    public void testDeleteRequeteSQLExceptionWhenRollback() throws DaoException, SQLException {
//        Mockito.when(dataSource.getConnection()).thenReturn(connection);
//        Mockito.when(connection.prepareStatement(Mockito.anyString())).thenReturn(mockPS);
//        Mockito.when(mockPS.executeUpdate()).thenThrow(new SQLException());
//        Mockito.doThrow(new SQLException()).when(connection).rollback();
//        try {
//            mockDao.delete(1);
//            Assert.fail();
//        } catch (DaoException expected) {
//            Assert.assertEquals(ComputerDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
//        }
//        Mockito.verify(connection).rollback();
//        Mockito.verify(dataSource).getConnection();
//        Mockito.verify(connection).prepareStatement(Mockito.anyString());
//        Mockito.verify(mockPS).executeUpdate();
//    }

    /**
     * Test de getPage.
     * @throws DaoException erreur de requête
     */
    @Test(expected = DaoException.class)
    public void testGetPageException() throws DaoException {
        Page<Computer> page = dao.getPage(1);
        Assert.assertTrue(page.getObjects().size() == page.getLimit());
        dao.getPage(-1);
    }

    /**
     * Test de getPageSearch.
     * @throws DaoException erreur de requête
     */
    @Test
    public void testGetPageSearchOk() throws DaoException {
        String search = "NomComputerPourTestGetPageSearchFE";
        Integer nbElements = 23;
        Set<Long> ids = new HashSet<Long>();
        for (int i = 0; i < nbElements; i++) {
            ids.add(dao.create(new Computer(search)));
        }
        Page<Computer> page = dao.getPageSearch(search, 1);
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
//
//    /**
//     * Test de getCount.
//     * @throws DaoException erreur de requête.
//     * @throws SQLException sql exception
//     */
//    @Test
//    public void testCountNextFalseReturnZero() throws DaoException, SQLException {
//        Mockito.when(dataSource.getConnection()).thenReturn(connection);
//        Mockito.when(connection.createStatement()).thenReturn(mockS);
//        Mockito.when(mockS.executeQuery(Mockito.anyString())).thenReturn(mockResultSet);
//        Mockito.when(mockResultSet.next()).thenReturn(false);
//        Assert.assertEquals(mockDao.getCount(), 0);
//        Mockito.verify(mockResultSet, Mockito.atLeastOnce()).next();
//        Mockito.verify(dataSource, Mockito.atLeastOnce()).getConnection();
//        Mockito.verify(connection, Mockito.atLeastOnce()).createStatement();
//        Mockito.verify(mockS).executeQuery(Mockito.anyString());
//    }

//    /**
//     * Test de getCount.
//     * @throws DaoException erreur de requête.
//     * @throws SQLException sql exception
//     */
//    @Test
//    public void testCountRequeteSQLException() throws DaoException, SQLException {
//        Mockito.when(dataSource.getConnection()).thenReturn(connection);
//        Mockito.when(connection.createStatement()).thenReturn(mockS);
//        Mockito.when(mockS.executeQuery(Mockito.anyString())).thenThrow(new SQLException());
//        try {
//            mockDao.getCount();
//            Assert.fail();
//        } catch (DaoException expected) {
//            Assert.assertEquals(ComputerDao.MESS_REQUEST_EXCEPTION, expected.getMessage());
//        }
//        Mockito.verify(dataSource).getConnection();
//        Mockito.verify(connection).createStatement();
//        Mockito.verify(mockS).executeQuery(Mockito.anyString());
//    }

}
