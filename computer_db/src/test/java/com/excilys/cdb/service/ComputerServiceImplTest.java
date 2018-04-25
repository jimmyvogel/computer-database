package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.service.exceptions.ServiceException;

public class ComputerServiceImplTest {

    private IComputerService service;
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

    private final LocalDateTime DD_INVALID_BEFORE_DI = LocalDateTime.of(1985,
            Month.APRIL, 8, 12, 30);

    private Company companyValid;
    private Computer computerValid;

    /**
     * Initialisation des données avant les tests.
     * @throws DAOConfigurationException erreur de configuration.
     * @throws ServiceException erreur de service.
     */
    @Before
    public void initialisation()
            throws DAOConfigurationException, ServiceException {
        MockitoAnnotations.initMocks(this);

        service = ComputerServiceImpl.getInstance();
        nbComputers = service.countComputers();
        nbCompany = service.countCompanies();

        assert (nbCompany > 0) : "Il faut des compagnies pour les tests";

        companyValid = service.getPageCompany(1).getObjects().get(0);
        computerValid = service.getPageComputer(1).getObjects().get(0);
    }

    /**
     * Test getAllComputer.
     * @throws ServiceException erreur de service.
     */
    @Test
    public void testGetAllComputer() throws ServiceException {

        List<Computer> computers = service.getAllComputer();
        Assert.assertEquals(computers.size(), nbComputers);

        Assert.assertTrue(service.getComputer(605) != null);
    }

    /**
     * Test de getAllCompany.
     * @throws ServiceException erreur de service.
     */
    @Test
    public void testGetAllCompany() throws ServiceException {
        List<Company> companies = service.getAllCompany();
        Assert.assertEquals(companies.size(), nbCompany);
    }

    /**
     * Test de getCompany.
     * @throws ServiceException erreur du service
     */
    @Test
    public void testGetCompany() throws ServiceException {
        List<Company> companies = service.getAllCompany();
        Company comp;
        for (Company c : companies) {
            comp = service.getCompany(c.getId());
            Assert.assertEquals(c.getName(), comp.getName());
        }
    }

    /**
     * Test de getComputer.
     * @throws ServiceException erreur de service.
     */
    @Test
    public void testGetComputer() throws ServiceException {
        List<Computer> computers = service.getAllComputer();
        Computer comp;
        for (Computer c : computers) {
            comp = service.getComputer(c.getId());
            Assert.assertEquals(c.getName(), comp.getName());
        }
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service
     */
    @Test
    public void testCreateComputerString() throws ServiceException {

        // Valid
        boolean create = service.createComputer(NAME_VALID) > 0;
        Assert.assertTrue(create);
        long count = service.countComputers();
        Assert.assertTrue(count == nbComputers + 1);

        // Invalid
        create = service.createComputer(NAME_INVALID) > 0;
        Assert.assertFalse(create);
        count = service.countComputers();
        Assert.assertTrue(count == nbComputers + 1);

    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service.
     */
    @Test
    public void testCreateComputerStringLocalDateTimeLocalDateTimeLong()
            throws ServiceException {
        Assert.assertTrue(nbCompany > 0);

        // Insertion valid
        boolean create = service.createComputer(NAME_VALID,
                DATE_INTRODUCED_VALID, DATE_DISCONTINUED_VALID,
                companyValid.getId()) > 0;
        Assert.assertTrue(create);
        long count = service.countComputers();
        Assert.assertTrue(count == nbComputers + 1);

        // Insertion invalid à cause du null
        create = service.createComputer(NAME_VALID, null,
                DATE_DISCONTINUED_VALID, companyValid.getId()) > 0;
        Assert.assertFalse(create);

        // Insertion invalid à cause du manque de tout
        create = service.createComputer(null, null, null, -1) > 0;
        Assert.assertFalse(create);

        // Insertion invalid à cause de la date discontinued avant la date
        // introduced.
        create = service.createComputer(NAME_VALID, DATE_INTRODUCED_VALID,
                DD_INVALID_BEFORE_DI, companyValid.getId()) > 0;
        Assert.assertFalse(create);

    }

    /**
     * Test de la méthode deleteComputer.
     * @throws ServiceException erreur du service.
     */
    @Test
    public void testDeleteComputer() throws ServiceException {
        Assert.assertTrue(nbComputers > 0);

        boolean delete = service.deleteComputer(computerValid.getId());
        Assert.assertTrue(delete);
        long count = service.countComputers();
        Assert.assertTrue(count == nbComputers - 1);
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur de service.
     */
    @Test
    public void testUpdateComputer() throws ServiceException {

        // Valid
        boolean update = service.updateComputer(computerValid.getId(),
                NAME_VALID);
        Assert.assertTrue(update);

        // Invalid
        update = service.updateComputer(computerValid.getId(), NAME_INVALID);
        Assert.assertFalse(update);

        // Reinitialisation
        service.updateComputer(computerValid.getId(), computerValid.getName());
    }

    /**
     * Méthode de test du updateComputer.
     * @throws ServiceException erreur sur le service
     */
    @Test
    public void testUpdateComputerLongStringLocalDateTimeLocalDateTimeLong()
            throws ServiceException {

        // Valid
        boolean update = service.updateComputer(computerValid.getId(),
                NAME_VALID, DATE_INTRODUCED_VALID, DATE_DISCONTINUED_VALID,
                companyValid.getId());
        Assert.assertTrue(update);

        // Valid
        update = service.updateComputer(computerValid.getId(), NAME_VALID,
                DATE_INTRODUCED_VALID, null, -1);
        Assert.assertTrue(update);

        // Valid
        update = service.updateComputer(computerValid.getId(), null,
                DATE_INTRODUCED_VALID, null, -1);
        Assert.assertTrue(update);

        // Valid
        update = service.updateComputer(computerValid.getId(), null,
                DATE_INTRODUCED_VALID, null, companyValid.getId());
        Assert.assertTrue(update);

        // Invalid with no arguments
        update = service.updateComputer(computerValid.getId(), null, null, null,
                -1);
        Assert.assertFalse(update);

        // Invalid with date not valid
        update = service.updateComputer(computerValid.getId(), null, null,
                Computer.BEGIN_DATE_VALID, -1);
        Assert.assertFalse(update);

        // Invalid with name not good
        update = service.updateComputer(computerValid.getId(), NAME_INVALID,
                null, null, -1);
        Assert.assertFalse(update);

        // Invalid with invalid id
        update = service.updateComputer(-1, null, null, null, -1);
        Assert.assertFalse(update);

        // Invalid with introduced before discontinued not valid
        update = service.updateComputer(computerValid.getId(), null,
                DATE_INTRODUCED_VALID, DD_INVALID_BEFORE_DI, -1);
        Assert.assertFalse(update);
    }

}
