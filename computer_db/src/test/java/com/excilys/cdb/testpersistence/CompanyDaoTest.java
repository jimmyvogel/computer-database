package com.excilys.cdb.testpersistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyCrudDao;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.testconfig.JunitSuite;
import com.excilys.cdb.testconfig.TestSpringConfig;
import com.zaxxer.hikari.HikariDataSource;

@SpringJUnitConfig(classes = TestSpringConfig.class)
public class CompanyDaoTest extends JunitSuite {

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

	@Autowired
	private static CompanyCrudDao dao;
	private Company companyValid;

//	/**
//	 * Initialisation.
//	 * @throws DaoConfigurationException erreur de configuration
//	 * @throws DaoException erreur de requête
//	 */
//	@Before
//	public void initialisation() throws DaoConfigurationException, DaoException {
//		MockitoAnnotations.initMocks(this);
//		companyValid = dao.getPage(1).getObjects().get(0);
//	}
//
//	/**
//	 * Test sur la méthode getAll.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test
//	public void testGetAll() throws DaoException {
//		long count = dao.getCount();
//		Assert.assertTrue(dao.getAll().size() == count);
//	}

	/**
	 */
	@BeforeClass
	public static void startContext() {
		// dao = (CompanyDao) context.getBean("companyDao");
	}

//	/**
//	 * Test de getByName.
//	 * @throws DaoException erreur de requête.
//	 */
//	@Test
//	public void testGetByName() throws DaoException {
//		String nameTest = "nameValidTestCompanyGetByNamedAutrefef";
//		long count = dao.getCount();
//		long id = dao.create(new Company(nameTest));
//		long id2 = dao.create(new Company(nameTest));
//		long id3 = dao.create(new Company(nameTest));
//		Assert.assertTrue(count + 3 == dao.getCount());
//		Assert.assertTrue(dao.getByName(nameTest).size() == 3);
//		// Suppression
//		List<Long> list = Arrays.asList(id, id2, id3);
//		Assert.assertTrue(dao.deleteAll(new HashSet<Long>(list)));
//	}
//
//	/**
//	 * Test sur la méthode getById.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test(expected = NoSuchElementException.class)
//	public void testGetById() throws DaoException {
//		Assert.assertTrue(dao.getById(companyValid.getId()) != null);
//		dao.getById(-1).get();
//	}

//	/**
//	 * Test sur la méthode getPage.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test(expected = DaoException.class)
//	public void testGetPage() throws DaoException {
//		CDBPage<Company> page = dao.getPage(1);
//		Assert.assertTrue(page.getObjects().size() == page.getLimit());
//		dao.getPage(-1);
//	}
//
//	/**
//	 * Test de getPageSearch.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test
//	public void testGetPageSearchOk() throws DaoException {
//		String search = "TestCompanyGetPageSearchefrefesf";
//		Integer nbElements = 23;
//		Set<Long> ids = new HashSet<Long>();
//		for (int i = 0; i < nbElements; i++) {
//			ids.add(dao.create(new Company(search)));
//		}
//		CDBPage<Company> page = dao.getPageSearch(search, 1);
//		Assert.assertTrue(page.getCount() == nbElements);
//
//		dao.deleteAll(ids);
//	}

//	/**
//	 * Test de getPageSearch.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test(expected = DaoException.class)
//	public void testGetPageSearchIndexException() throws DaoException {
//		dao.getPageSearch("nomauhasard", -1);
//	}
//
//	/**
//	 * Test sur la méthode create.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test
//	public void testCreateOk() throws DaoException {
//		long count = dao.getCount();
//		dao.create(new Company("nomValid"));
//		Assert.assertTrue(count + 1 == dao.getCount());
//	}
//
//	/**
//	 * Test sur la méthode create.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test(expected = DaoException.class)
//	public void testCreateNameNull() throws DaoException {
//		dao.create(new Company(null));
//	}
//
//	/**
//	 * Test sur la méthode delete.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test
//	public void testDeleteOneElement() throws DaoException {
//		long count = dao.getCount();
//		long id = dao.create(new Company("nameValid"));
//		Assert.assertTrue(count + 1 == dao.getCount());
//		Assert.assertTrue(dao.delete(id));
//	}

	// /**
	// * Test de delete.
	// * @throws DaoException
	// * erreur de requête.
	// * @throws SQLException
	// * sql exception
	// */
	// @Test
	// public void testDeleteRequeteSQLException()
	// throws DaoException, SQLException {
	// Mockito.when(dataSource.getConnection()).thenReturn(connection);
	// Mockito.when(connection.prepareStatement(Mockito.anyString()))
	// .thenReturn(mockPS);
	// Mockito.when(mockPS.executeUpdate()).thenThrow(new SQLException());
	// try {
	// mockDao.delete(1);
	// Assert.fail();
	// } catch (DaoException expected) {
	// Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION,
	// expected.getMessage());
	// }
	// Mockito.verify(dataSource).getConnection();
	// Mockito.verify(connection,
	// Mockito.atMost(2)).prepareStatement(Mockito.anyString());
	// Mockito.verify(mockPS, Mockito.atMost(2)).executeUpdate();
	// }

	/**
	 * Test de delete.
	 * @throws DaoException erreur de requête.
	 * @throws SQLException sql exception
	 */
	// @Test
	// public void testDeleteIdAbsent() throws DaoException, SQLException {
	// Mockito.when(dataSource.getConnection()).thenReturn(connection);
	// Mockito.when(connection.prepareStatement(Mockito.anyString()))
	// .thenReturn(mockPS);
	// Mockito.when(mockPS.executeUpdate()).thenReturn(-1);
	// Assert.assertFalse(mockDao.delete(1));
	// Mockito.verify(dataSource).getConnection();
	// Mockito.verify(connection, Mockito.atMost(2))
	// .prepareStatement(Mockito.anyString());
	// Mockito.verify(mockPS, Mockito.atMost(2)).executeUpdate();
	// }

//	/**
//	 * Test sur la méthode delete.
//	 * @throws DaoException erreur de requête
//	 */
//	@Test
//	public void testDeleteManyElement() throws DaoException {
//		long count = dao.getCount();
//		long id = dao.create(new Company("nameValid"));
//		long id2 = dao.create(new Company("nameValid2"));
//		long id3 = dao.create(new Company("nameValid3"));
//		Assert.assertTrue(count + 3 == dao.getCount());
//		List<Long> list = Arrays.asList(id, id2, id3);
//		HashSet<Long> set = new HashSet<Long>(list);
//		Assert.assertNotNull(set);
//		Assert.assertTrue(dao.deleteAll(set));
//	}

	// /**
	// * Test de getCount.
	// * @throws DaoException
	// * erreur de requête.
	// * @throws SQLException
	// * sql exception
	// */
	// @Test
	// public void testCountNextFalseReturnZero()
	// throws DaoException, SQLException {
	// Mockito.when(dataSource.getConnection()).thenReturn(connection);
	// Mockito.when(connection.createStatement()).thenReturn(mockS);
	// Mockito.when(mockS.executeQuery(Mockito.anyString()))
	// .thenReturn(mockResultSet);
	// Mockito.when(mockResultSet.next()).thenReturn(false);
	// Assert.assertEquals(mockDao.getCount(), 0);
	// Mockito.verify(mockResultSet, Mockito.atLeastOnce()).next();
	// Mockito.verify(dataSource, Mockito.atLeastOnce()).getConnection();
	// Mockito.verify(connection, Mockito.atLeastOnce()).createStatement();
	// Mockito.verify(mockS).executeQuery(Mockito.anyString());
	// }

	// /**
	// * Test de getAll.
	// * @throws DaoException
	// * erreur de requête.
	// * @throws SQLException
	// * sql exception
	// */
	// @Test
	// public void testCountRequeteSQLException()
	// throws DaoException, SQLException {
	// Mockito.when(dataSource.getConnection()).thenReturn(connection);
	// Mockito.when(connection.createStatement()).thenReturn(mockS);
	// Mockito.when(mockS.executeQuery(Mockito.anyString()))
	// .thenThrow(new SQLException());
	// try {
	// mockDao.getCount();
	// Assert.fail();
	// } catch (DaoException expected) {
	// Assert.assertEquals(CompanyDao.MESS_REQUEST_EXCEPTION,
	// expected.getMessage());
	// }
	// Mockito.verify(dataSource).getConnection();
	// Mockito.verify(connection).createStatement();
	// Mockito.verify(mockS).executeQuery(Mockito.anyString());
	// }
}
