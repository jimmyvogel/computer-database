package com.excilys.cdb.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

import com.excilys.cdb.config.ApplicationSpringConfig;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.CompanyNotFoundException;
import com.excilys.cdb.persistence.exceptions.ComputerNotFoundException;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.DateInvalidException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.ComputerValidator;

public class ComputerServiceTest {

    private static ComputerService serviceComputer;
    private static CompanyService serviceCompany;

    private long nbComputers;
    private long nbCompany;

    private final String NAME_VALID = "nouveaunomvalid";

    private final String NAME_INVALID = "ffff ffff ffff ffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff fffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
            + "ffff ffff ffff fffff" + "ffffffffffffffffffff";

    private final LocalDateTime DATE_INTRODUCED_VALID = LocalDateTime.of(1986,
            Month.APRIL, 8, 12, 30);

    private final LocalDateTime DATE_DISCONTINUED_VALID = LocalDateTime.of(2000,
            Month.APRIL, 8, 12, 30);

    private final LocalDateTime DI_INVALID_AFTER_DD = LocalDateTime.of(2001,
            Month.APRIL, 8, 12, 30);

    private final LocalDateTime DD_INVALID_BEFORE_DI = LocalDateTime.of(1985,
            Month.APRIL, 8, 12, 30);

    private Company companyValid;
    private Computer computerValid;

    private static ApplicationSpringConfig appConfig;

    /**
     */
    @BeforeClass
    public static void startContext() {
    	appConfig = new ApplicationSpringConfig();
    	serviceComputer = (ComputerService) appConfig.getAppContext().getBean("computerService");
    	serviceCompany = (CompanyService) appConfig.getAppContext().getBean("companyService");
    }

    /**
     * Initialisation des données avant les tests.
     * @throws DAOConfigurationException erreur de configuration.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Before
    public void initialisation()
            throws DAOConfigurationException, ServiceException, DaoException {
        MockitoAnnotations.initMocks(this);
        nbComputers = serviceComputer.countComputers();
        nbCompany = serviceCompany.countCompanies();

        assert (nbCompany > 0) : "Il faut des compagnies pour les tests";

        companyValid = serviceCompany.getPageCompany(1).getObjects().get(0);
        computerValid = serviceComputer.getPageComputer(1).getObjects().get(0);
        computerValid.setCompany(companyValid);
        serviceComputer.updateComputer(computerValid.getId(),
                computerValid.getName(),
                computerValid.getIntroduced(),
                computerValid.getDiscontinued(),
                computerValid.getCompany().getId());
    }

    /**
     * Test getAllComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de requête.
     */
    @Test
    public void testGetAllComputer() throws ServiceException, DaoException {

        List<Computer> computers = serviceComputer.getAllComputer();
        Assert.assertEquals(computers.size(), nbComputers);
    }

    /**
     * Test de getComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testGetComputer() throws ServiceException, DaoException {
        List<Computer> computers = serviceComputer.getAllComputer();
        Computer comp;
        for (Computer c : computers) {
            comp = serviceComputer.getComputer(c.getId());
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
        boolean create = serviceComputer.createComputer(NAME_VALID) > 0;
        Assert.assertTrue(create);
        long count = serviceComputer.countComputers();
        Assert.assertTrue(count == nbComputers + 1);
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = NameInvalidException.class)
    public void testCreateComputerStringFailNameInvalid() throws ServiceException, DaoException {
        serviceComputer.createComputer(NAME_INVALID);
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testCreateComputerStringLocalDateTimeLocalDateTimeLong()
            throws ServiceException, DaoException {
        Assert.assertTrue(nbCompany > 0);

        // Insertion valid
        boolean create = serviceComputer.createComputer(NAME_VALID,
                DATE_INTRODUCED_VALID, DATE_DISCONTINUED_VALID,
                companyValid.getId()) > 0;
        Assert.assertTrue(create);
        long count = serviceComputer.countComputers();
        Assert.assertTrue(count == nbComputers + 1);

    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = DateInvalidException.class)
    public void testCreateComputerFailDateDiscontinuedBeforeIntroduced()
            throws ServiceException, DaoException {
        serviceComputer.createComputer(NAME_VALID, DATE_INTRODUCED_VALID,
                DD_INVALID_BEFORE_DI, companyValid.getId());
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = NameInvalidException.class)
    public void testCreateComputerNameEmpty()
            throws ServiceException, DaoException {
        serviceComputer.createComputer(null, null, null, -1);
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = DateInvalidException.class)
    public void testCreateComputerFailDiscontinuedWithoutIntroduced()
            throws ServiceException, DaoException {
        serviceComputer.createComputer(NAME_VALID, null,
                DATE_DISCONTINUED_VALID, companyValid.getId());
    }

    /**
     * Test de la méthode deleteComputer.
     * @throws ServiceException erreur du service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteComputer() throws ServiceException, DaoException {
        Assert.assertTrue(nbComputers > 0);

        boolean delete = serviceComputer.deleteComputer(computerValid.getId());
        Assert.assertTrue(delete);
        long count = serviceComputer.countComputers();
        Assert.assertTrue(count == nbComputers - 1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testUpdateComputer() throws ServiceException, DaoException {

        // Valid
        boolean update = serviceComputer.updateComputer(computerValid.getId(),
                NAME_VALID);
        Assert.assertTrue(update);

        // Reinitialisation
        serviceComputer.updateComputer(computerValid.getId(), computerValid.getName());
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = NameInvalidException.class)
    public void testUpdateComputerNameTooLong() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), NAME_INVALID);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = NameInvalidException.class)
    public void testUpdateComputerWithNameNull() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), null);
    }


    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testUpdateComputerLongStringLocalDateTimeLocalDateTimeLong()
            throws ServiceException, DaoException {

        // Valid
        boolean update = serviceComputer.updateComputer(computerValid.getId(),
                NAME_VALID, DATE_INTRODUCED_VALID, DATE_DISCONTINUED_VALID,
                companyValid.getId());
        Assert.assertTrue(update);

        // Valid
        update = serviceComputer.updateComputer(computerValid.getId(), NAME_VALID,
                DATE_INTRODUCED_VALID, null, -1);
        Assert.assertTrue(update);

        // Valid
        update = serviceComputer.updateComputer(computerValid.getId(), null,
                DATE_INTRODUCED_VALID, null, -1);
        Assert.assertTrue(update);

        // Valid
        update = serviceComputer.updateComputer(computerValid.getId(), null,
                DATE_INTRODUCED_VALID, null, companyValid.getId());
        Assert.assertTrue(update);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateComputerInvalidName()
            throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), NAME_INVALID, null, null, -1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateComputerInvalidIdCompany()
            throws ServiceException, DaoException {
        serviceComputer.updateComputer(-1, null, null, null, -1);
    }


    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateInvalidDateIntroducedMinDate() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), null, ComputerValidator.BEGIN_DATE_VALID, null, -1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateInvalidDateIntroducedMaxDate() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), null, ComputerValidator.END_DATE_VALID, null, -1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateInvalidDateDiscontinuedMinDate() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), null, null, ComputerValidator.BEGIN_DATE_VALID, -1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateInvalidDateDiscontinuedMaxDate() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), null, null, ComputerValidator.END_DATE_VALID, -1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateDiscontinuedWithoutIntroduced() throws ServiceException, DaoException {

        long id = serviceComputer.createComputer("nouveau");
        assertTrue(id > 0);
        Computer c = serviceComputer.getComputer(id);
        assertTrue(c != null);

        assertFalse(serviceComputer.updateComputer(id, null, null, DATE_DISCONTINUED_VALID, -1));
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateDiscontinuedBeforeIntroduced() throws ServiceException, DaoException {

        long id = serviceComputer.createComputer("nouveau");
        assertTrue(id > 0);
        Computer c = serviceComputer.getComputer(id);
        assertTrue(c != null);
        assertFalse(serviceComputer.updateComputer(id,  null, DATE_INTRODUCED_VALID, DD_INVALID_BEFORE_DI, -1));
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateDiscontinuedBeforeInitialIntroduced() throws ServiceException, DaoException {

        long id = serviceComputer.createComputer("nouveau");
        assertTrue(id > 0);
        Computer c = serviceComputer.getComputer(id);
        assertTrue(c != null);

        assertTrue(serviceComputer.updateComputer(id,  null, DATE_INTRODUCED_VALID, null, -1));
        assertFalse(serviceComputer.updateComputer(id,  null, null, DD_INVALID_BEFORE_DI, -1));
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testUpdateIntroducedNotNecessary() throws ServiceException, DaoException {

        long id = serviceComputer.createComputer("nouveau");
        assertTrue(id > 0);
        Computer c = serviceComputer.getComputer(id);
        assertTrue(c != null);

        assertTrue(serviceComputer.updateComputer(id, null, null, null, companyValid.getId()));

        assertTrue(serviceComputer.deleteComputer(id));
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateWithoutArguments() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), null, null, null, -1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testUpdateWithCompany0() throws ServiceException, DaoException {

        boolean update = serviceComputer.updateComputer(computerValid.getId(), null, null, null,
                0);
        Assert.assertTrue(update);
        Assert.assertNull(serviceComputer.getComputer(computerValid.getId()).getCompany());
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = CompanyNotFoundException.class)
    public void testUpdateFailWithCompanyMaxInexistant() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), null, null, null, Long.MAX_VALUE);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testUpdateNoModifCompanyWithMoinsUn() throws ServiceException, DaoException {
        boolean update = serviceComputer.updateComputer(computerValid.getId(), computerValid.getName(), null, null,
                -1);
        Assert.assertTrue(update);
        Assert.assertTrue(serviceComputer.getComputer(computerValid.getId()).getCompany().equals(computerValid.getCompany()));
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ServiceException.class)
    public void testUpdateIntroducedAfterDiscontinuedExistant() throws ServiceException, DaoException {
        serviceComputer.updateComputer(computerValid.getId(), computerValid.getName(), DI_INVALID_AFTER_DD, null, -1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ComputerNotFoundException.class)
    public void testUpdateFailWithBadId() throws ServiceException, DaoException {

        serviceComputer.updateComputer(-1, computerValid.getName(), null, null, -1);
    }

    /**
     * Méthode de test du deleteComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteWithBadId() throws ServiceException, DaoException {
        assertFalse(serviceComputer.deleteComputer(-1));
        assertFalse(serviceComputer.deleteComputer(Long.MAX_VALUE));
    }

    /**
     * Méthode de test du deleteComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteManyComputerOk() throws ServiceException, DaoException {
        long count = serviceComputer.countComputers();
        long id = serviceComputer.createComputer(NAME_VALID);
        long id2 = serviceComputer.createComputer(NAME_VALID);
        long id3 = serviceComputer.createComputer(NAME_VALID);
        assertTrue(count + 3 == serviceComputer.countComputers());
        assertTrue(serviceComputer.deleteComputers(new HashSet<Long>(Arrays.asList(id, id2, id3))));
        assertTrue(count == serviceComputer.countComputers());
    }

    /**
     * Méthode de test du deleteComputers.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteManyComputerWithBadList() throws ServiceException, DaoException {
        assertFalse(serviceComputer.deleteComputers(null));
        assertFalse(serviceComputer.deleteComputers(new HashSet<Long>()));
    }

}
