package com.excilys.cdb.testservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.exceptions.CompanyNotFoundException;
import com.excilys.cdb.service.exceptions.ComputerNotFoundException;
import com.excilys.cdb.service.exceptions.DateInvalidException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.testconfig.JunitSuite;
import com.excilys.cdb.testconfig.TestSpringConfig;
import com.excilys.cdb.validator.ComputerValidator;

@SpringJUnitConfig(classes = TestSpringConfig.class)
public class ComputerServiceTest extends JunitSuite {

	@Autowired
	private ComputerService serviceComputer;
	@Autowired
	private CompanyService serviceCompany;

	private long nbComputers;
	private long nbCompany;

	private final String NAME_VALID = "nouveaunomvalid";

	private final String NAME_INVALID = "ffff ffff ffff ffff" + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
			+ "ffff ffff ffff ffff" + "ffff ffff ffff fffff" + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
			+ "ffff ffff ffff ffff" + "ffff ffff ffff ffff" + "ffff ffff ffff fffff" + "ffffffffffffffffffff";

	private final LocalDate DATE_INTRODUCED_VALID = LocalDate.of(1986, Month.APRIL, 8);

	private final LocalDate DATE_DISCONTINUED_VALID = LocalDate.of(2000, Month.APRIL, 8);

	private final LocalDate DI_INVALID_AFTER_DD = LocalDate.of(2001, Month.APRIL, 8);

	private final LocalDate DD_INVALID_BEFORE_DI = LocalDate.of(1985, Month.APRIL, 8);

	private Company companyValid;
	private Computer computerValid;

	/**
	 */
	@BeforeClass
	public static void startContext() {
		// serviceComputer = (IComputerService) context.getBean("iComputerService");
		// serviceCompany = (ICompanyService) context.getBean("iCompanyService");
	}

	/**
	 * Initialisation des données avant les tests.
	 * @throws DaoConfigurationException erreur de configuration.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Before
	public void initialisation() throws DaoConfigurationException, ServiceException, DaoException {
		MockitoAnnotations.initMocks(this);
		nbComputers = serviceComputer.count();
		nbCompany = serviceCompany.count();

		assert (nbCompany > 0) : "Il faut des compagnies pour les tests";

		companyValid = serviceCompany.getPage(1).getObjects().get(0);
		computerValid = serviceComputer.getPage(1, null).getObjects().get(0);
		computerValid.setCompany(companyValid);
		serviceComputer.update(computerValid.getId(), computerValid.getName(), computerValid.getIntroduced(),
				computerValid.getDiscontinued(), computerValid.getCompany().getId());
	}

	/**
	 * Test getAllComputer.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de requête.
	 */
	@Test
	public void testGetAllComputer() throws ServiceException, DaoException {

		List<Computer> computers = serviceComputer.getAll();
		Assert.assertEquals(computers.size(), nbComputers);
	}

	/**
	 * Test de getComputer.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testGetComputer() throws ServiceException, DaoException {
		List<Computer> computers = serviceComputer.getAll();
		Computer comp;
		for (Computer c : computers) {
			comp = serviceComputer.get(c.getId());
			Assert.assertEquals(c.getName(), comp.getName());
		}
	}

	/**
	 * Test de createComputer.
	 * @throws ServiceException erreur de service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testCreateComputerStringOk() throws ServiceException, DaoException {
		boolean create = serviceComputer.create(NAME_VALID) > 0;
		Assert.assertTrue(create);
		long count = serviceComputer.count();
		Assert.assertTrue(count == nbComputers + 1);
	}

	/**
	 * Test de createComputer.
	 * @throws ServiceException erreur de service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = NameInvalidException.class)
	public void testCreateComputerStringFailNameInvalid() throws ServiceException, DaoException {
		serviceComputer.create(NAME_INVALID);
	}

	/**
	 * Test de createComputer.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testCreateComputerStringLocalDateTimeLocalDateTimeLong() throws ServiceException, DaoException {
		Assert.assertTrue(nbCompany > 0);

		// Insertion valid
		boolean create = serviceComputer.create(NAME_VALID, DATE_INTRODUCED_VALID, DATE_DISCONTINUED_VALID,
				companyValid.getId()) > 0;
		Assert.assertTrue(create);
		long count = serviceComputer.count();
		Assert.assertTrue(count == nbComputers + 1);

	}

	/**
	 * Test de createComputer.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = DateInvalidException.class)
	public void testCreateComputerFailDateDiscontinuedBeforeIntroduced() throws ServiceException, DaoException {
		serviceComputer.create(NAME_VALID, DATE_INTRODUCED_VALID, DD_INVALID_BEFORE_DI, companyValid.getId());
	}

	/**
	 * Test de createComputer.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testCreateComputerNameEmpty() throws ServiceException, DaoException {
		serviceComputer.create(null, null, null, -1);
	}

	/**
	 * Test de createComputer.
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = DateInvalidException.class)
	public void testCreateComputerFailDiscontinuedWithoutIntroduced() throws ServiceException, DaoException {
		serviceComputer.create(NAME_VALID, null, DATE_DISCONTINUED_VALID, companyValid.getId());
	}

	/**
	 * Test de la méthode deleteComputer.
	 * @throws ServiceException erreur du service.
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testDeleteComputer() throws ServiceException, DaoException {
		Assert.assertTrue(nbComputers > 0);

		serviceComputer.delete(computerValid.getId());
		long count = serviceComputer.count();
		Assert.assertTrue(count == nbComputers - 1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testUpdateComputerLongStringLocalDateTimeLocalDateTimeLong() throws ServiceException, DaoException {

		// Valid
		boolean update = serviceComputer.update(computerValid.getId(), NAME_VALID, DATE_INTRODUCED_VALID,
				DATE_DISCONTINUED_VALID, companyValid.getId());
		Assert.assertTrue(update);

		// Valid
		update = serviceComputer.update(computerValid.getId(), NAME_VALID, DATE_INTRODUCED_VALID, null, -1);
		Assert.assertTrue(update);

		// Valid
		update = serviceComputer.update(computerValid.getId(), null, DATE_INTRODUCED_VALID, null, -1);
		Assert.assertTrue(update);

		// Valid
		update = serviceComputer.update(computerValid.getId(), null, DATE_INTRODUCED_VALID, null, companyValid.getId());
		Assert.assertTrue(update);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateComputerInvalidName() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), NAME_INVALID, null, null, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateComputerInvalidIdCompany() throws ServiceException, DaoException {
		serviceComputer.update(-1, null, null, null, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateInvalidDateIntroducedMinDate() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), null, ComputerValidator.BEGIN_DATE_VALID, null, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateInvalidDateIntroducedMaxDate() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), null, ComputerValidator.END_DATE_VALID, null, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateInvalidDateDiscontinuedMinDate() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), null, null, ComputerValidator.BEGIN_DATE_VALID, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateInvalidDateDiscontinuedMaxDate() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), null, null, ComputerValidator.END_DATE_VALID, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateDiscontinuedWithoutIntroduced() throws ServiceException, DaoException {

		long id = serviceComputer.create("nouveau");
		assertTrue(id > 0);
		Computer c = serviceComputer.get(id);
		assertTrue(c != null);

		assertFalse(serviceComputer.update(id, null, null, DATE_DISCONTINUED_VALID, -1));
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateDiscontinuedBeforeIntroduced() throws ServiceException, DaoException {

		long id = serviceComputer.create("nouveau");
		assertTrue(id > 0);
		Computer c = serviceComputer.get(id);
		assertTrue(c != null);
		assertFalse(serviceComputer.update(id, null, DATE_INTRODUCED_VALID, DD_INVALID_BEFORE_DI, -1));
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateDiscontinuedBeforeInitialIntroduced() throws ServiceException, DaoException {

		long id = serviceComputer.create("nouveau");
		assertTrue(id > 0);
		Computer c = serviceComputer.get(id);
		assertTrue(c != null);

		assertTrue(serviceComputer.update(id, null, DATE_INTRODUCED_VALID, null, -1));
		assertFalse(serviceComputer.update(id, null, null, DD_INVALID_BEFORE_DI, -1));
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testUpdateIntroducedNotNecessary() throws ServiceException, DaoException {

		long id = serviceComputer.create("nouveau");
		assertTrue(id > 0);
		Computer c = serviceComputer.get(id);
		assertTrue(c != null);

		assertTrue(serviceComputer.update(id, null, null, null, companyValid.getId()));

		serviceComputer.delete(id);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateWithoutArguments() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), null, null, null, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testUpdateWithCompany0() throws ServiceException, DaoException {

		boolean update = serviceComputer.update(computerValid.getId(), null, null, null, 0);
		Assert.assertTrue(update);
		Assert.assertNull(serviceComputer.get(computerValid.getId()).getCompany());
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = CompanyNotFoundException.class)
	public void testUpdateFailWithCompanyMaxInexistant() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), null, null, null, Long.MAX_VALUE);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testUpdateNoModifCompanyWithMoinsUn() throws ServiceException, DaoException {
		boolean update = serviceComputer.update(computerValid.getId(), computerValid.getName(), null, null, -1);
		Assert.assertTrue(update);
		Assert.assertTrue(serviceComputer.get(computerValid.getId()).getCompany().equals(computerValid.getCompany()));
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testUpdateIntroducedAfterDiscontinuedExistant() throws ServiceException, DaoException {
		serviceComputer.update(computerValid.getId(), computerValid.getName(), DI_INVALID_AFTER_DD, null, -1);
	}

	/**
	 * Méthode de test du updateComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ComputerNotFoundException.class)
	public void testUpdateFailWithBadId() throws ServiceException, DaoException {

		serviceComputer.update(-1, computerValid.getName(), null, null, -1);
	}

	/**
	 * Méthode de test du deleteComputer.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test
	public void testDeleteManyComputerOk() throws ServiceException, DaoException {
		long count = serviceComputer.count();
		long id = serviceComputer.create(NAME_VALID);
		long id2 = serviceComputer.create(NAME_VALID);
		long id3 = serviceComputer.create(NAME_VALID);
		assertTrue(count + 3 == serviceComputer.count());
		serviceComputer.deleteAll(new HashSet<Long>(Arrays.asList(id, id2, id3)));
		assertTrue(count == serviceComputer.count());
	}

	/**
	 * Méthode de test du deleteComputers.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testDeleteManyComputerWithEmptyList() throws ServiceException, DaoException {
		serviceComputer.deleteAll(new HashSet<Long>());
	}

	/**
	 * Méthode de test du deleteComputers.
	 * @throws ServiceException erreur sur le service
	 * @throws DaoException erreur de reqûete.
	 */
	@Test(expected = ServiceException.class)
	public void testDeleteManyComputerWithBadList() throws ServiceException, DaoException {
		serviceComputer.deleteAll(null);
	}

}
