package com.excilys.cdb.testservice;

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

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.exceptions.ComputerNotFoundException;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.testconfig.TestSuite;

public class CompanyServiceTest extends TestSuite {

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


    /**
     */
    @BeforeClass
    public static void startContext() {
    	serviceComputer = (ComputerService) context.getBean("computerService");
    	serviceCompany = (CompanyService) context.getBean("companyService");
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
        nbCompany = serviceCompany.count();

        assert (nbCompany > 0) : "Il faut des compagnies pour les tests";

        companyValid = serviceCompany.getPage(1).getObjects().get(0);
        computerValid = serviceComputer.getPage(1).getObjects().get(0);
        computerValid.setCompany(companyValid);
        serviceComputer.update(computerValid.getId(),
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
        List<Company> companies = serviceCompany.getAll();
        Assert.assertEquals(companies.size(), nbCompany);
    }

    /**
     * Test de getCompany.
     * @throws ServiceException erreur du service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testGetCompany() throws ServiceException, DaoException {
        List<Company> companies = serviceCompany.getAll();
        Company comp;
        for (Company c : companies) {
            comp = serviceCompany.get(c.getId());
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
        boolean create = serviceCompany.create(NAME_VALID) > 0;
        Assert.assertTrue(create);
        long count = serviceCompany.count();
        Assert.assertTrue(count == nbCompany + 1);
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = NameInvalidException.class)
    public void testCreateCompanyStringFailNameInvalid() throws ServiceException, DaoException {
        serviceCompany.create(NAME_INVALID);
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ComputerNotFoundException.class)
    public void testDeleteOneCompanyWithLinkedComputersOk() throws ServiceException, DaoException {
        long count = serviceCompany.count();
        long id = serviceCompany.create(NAME_VALID);
        assertTrue(count + 1 == serviceCompany.count());
        long idC = serviceComputer.create(NAME_VALID, null, null, id);
        Assert.assertNotNull(serviceComputer.get(idC));
        assertTrue(serviceCompany.delete(id));
        serviceComputer.get(idC);
    }

    /**
     * Méthode de test du deleteComputer.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteManyCompaniesWithoutLinkedComputersOk() throws ServiceException, DaoException {
        long count = serviceCompany.count();
        long id = serviceCompany.create(NAME_VALID);
        long id2 = serviceCompany.create(NAME_VALID);
        long id3 = serviceCompany.create(NAME_VALID);
        assertTrue(count + 3 == serviceCompany.count());
        assertTrue(serviceCompany.deleteAll(new HashSet<Long>(Arrays.asList(id, id2, id3))));
        assertTrue(count == serviceCompany.count());
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test(expected = ComputerNotFoundException.class)
    public void testDeleteManyCompaniesWithLinkedComputersOk() throws ServiceException, DaoException {
        long count = serviceCompany.count();
        long id = serviceCompany.create(NAME_VALID);
        assertTrue(count + 1 == serviceCompany.count());
        long idC = serviceComputer.create(NAME_VALID, null, null, id);
        Assert.assertNotNull(serviceComputer.get(idC));
        assertTrue(serviceCompany.deleteAll(new HashSet<Long>(Arrays.asList(id))));
        serviceComputer.get(idC);
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     * @throws DaoException erreur de reqûete.
     */
    @Test
    public void testDeleteManyCompanyWithBadList() throws ServiceException, DaoException {
        assertFalse(serviceCompany.deleteAll(null));
        assertFalse(serviceCompany.deleteAll(new HashSet<Long>()));
    }

}
