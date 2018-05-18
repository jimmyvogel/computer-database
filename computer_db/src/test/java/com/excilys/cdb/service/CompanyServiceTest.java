package com.excilys.cdb.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.excilys.cdb.persistence.exceptions.ComputerNotFoundException;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;

public class CompanyServiceTest {

    private static ComputerService serviceComputer;
    private static CompanyService serviceCompany;
    private long nbCompany;

    private final String NAME_VALID = "nouveaunomvalid";

    private final String NAME_INVALID = "ffff ffff ffff ffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff fffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
            + "ffff ffff ffff fffff" + "ffffffffffffffffffff"
            + "ffff ffff ffff ffff" + "ffff ffff ffff ffff"
            + "ffff ffff ffff fffff" + "ffffffffffffffffffff";

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
     * Test de getAllCompany.
     * @throws ServiceException erreur de service.
     */
    @Test
    public void testGetAllCompany() throws ServiceException {
        List<Company> companies = serviceCompany.getAllCompany();
        Assert.assertEquals(companies.size(), nbCompany);
    }

    /**
     * Test de getCompany.
     * @throws ServiceException erreur du service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testGetCompany() throws ServiceException, DaoException {
        List<Company> companies = serviceCompany.getAllCompany();
        Company comp;
        for (Company c : companies) {
            comp = serviceCompany.getCompany(c.getId());
            Assert.assertEquals(c.getName(), comp.getName());
        }
    }

    /**
     * Test de createCompany.
     * @throws ServiceException erreur de service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testCreateCompanyStringOk() throws ServiceException, DaoException {
        boolean create = serviceCompany.createCompany(NAME_VALID) > 0;
        Assert.assertTrue(create);
        long count = serviceCompany.countCompanies();
        Assert.assertTrue(count == nbCompany + 1);
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = NameInvalidException.class)
    public void testCreateCompanyStringFailNameInvalid() throws ServiceException, DaoException {
        serviceCompany.createCompany(NAME_INVALID);
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ComputerNotFoundException.class)
    public void testDeleteOneCompanyWithLinkedComputersOk() throws ServiceException, DaoException {
        long count = serviceCompany.countCompanies();
        long id = serviceCompany.createCompany(NAME_VALID);
        assertTrue(count + 1 == serviceCompany.countCompanies());
        long idC = serviceComputer.createComputer(NAME_VALID, null, null, id);
        Assert.assertNotNull(serviceComputer.getComputer(idC));
        assertTrue(serviceCompany.deleteCompany(id));
        serviceComputer.getComputer(idC);
    }

    /**
     * Méthode de test du deleteComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteManyCompaniesWithoutLinkedComputersOk() throws ServiceException, DaoException {
        long count = serviceCompany.countCompanies();
        long id = serviceCompany.createCompany(NAME_VALID);
        long id2 = serviceCompany.createCompany(NAME_VALID);
        long id3 = serviceCompany.createCompany(NAME_VALID);
        assertTrue(count + 3 == serviceCompany.countCompanies());
        assertTrue(serviceCompany.deleteCompanies(new HashSet<Long>(Arrays.asList(id, id2, id3))));
        assertTrue(count == serviceCompany.countCompanies());
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ComputerNotFoundException.class)
    public void testDeleteManyCompaniesWithLinkedComputersOk() throws ServiceException, DaoException {
        long count = serviceCompany.countCompanies();
        long id = serviceCompany.createCompany(NAME_VALID);
        assertTrue(count + 1 == serviceCompany.countCompanies());
        long idC = serviceComputer.createComputer(NAME_VALID, null, null, id);
        Assert.assertNotNull(serviceComputer.getComputer(idC));
        assertTrue(serviceCompany.deleteCompanies(new HashSet<Long>(Arrays.asList(id))));
        serviceComputer.getComputer(idC);
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteManyCompanyWithBadList() throws ServiceException, DaoException {
        assertFalse(serviceCompany.deleteCompanies(null));
        assertFalse(serviceCompany.deleteCompanies(new HashSet<Long>()));
    }

}
