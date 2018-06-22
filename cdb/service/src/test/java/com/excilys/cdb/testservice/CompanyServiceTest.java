package com.excilys.cdb.testservice;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.service.exceptions.ComputerNotFoundException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;

public class CompanyServiceTest extends JunitSuite {
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

    private CompanyDTO companyValid;
    private ComputerDTO computerValid;

    /**
     * Initialisation des données avant les tests.
     * @throws DaoConfigurationException erreur de configuration.
     * @throws ServiceException erreur de service.
     */
    @Before
    public void initialisation() throws ServiceException{
        nbCompany = serviceCompany.count();

        assert (nbCompany > 0) : "Il faut des compagnies pour les tests";
        companyValid = serviceCompany.getPage(1).getContent().get(0);
        computerValid = serviceComputer.getPage(1, null).getContent().get(0);
        computerValid.setCompanyId(companyValid.getId());;
        serviceComputer.update(computerValid.getId(),
                computerValid.getName(),
                computerValid.getIntroduced(),
                computerValid.getDiscontinued(),
                computerValid.getCompanyId());
    }

    /**
     * Test de getAllCompany.
     * @throws ServiceException erreur de service.
     */
    @Test
    public void testGetAllCompany() throws ServiceException {
        List<CompanyDTO> companies = serviceCompany.getAllLazy();
        Assert.assertEquals(companies.size(), nbCompany);
    }

    /**
     * Test de getCompany.
     * @throws ServiceException erreur du service
     */
    @Test
    public void testGetCompany() throws ServiceException {
        List<CompanyDTO> companies = serviceCompany.getAllLazy();
        CompanyDTO comp;
        for (CompanyDTO c : companies) {
            comp = serviceCompany.get(c.getId());
            Assert.assertEquals(c.getName(), comp.getName());
        }
    }

    /**
     * Test de createCompany.
     * @throws ServiceException erreur de service
     */
    @Test
    public void testCreateCompanyStringOk() throws ServiceException {
        boolean create = serviceCompany.create(NAME_VALID) > 0;
        Assert.assertTrue(create);
        long count = serviceCompany.count();
        Assert.assertTrue(count == nbCompany + 1);
    }

    /**
     * Test de createComputer.
     * @throws ServiceException erreur de service
     */
    @Test(expected = NameInvalidException.class)
    public void testCreateCompanyStringFailNameInvalid() throws ServiceException {
        serviceCompany.create(NAME_INVALID);
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     */
    @Test(expected = ComputerNotFoundException.class)
    public void testDeleteOneCompanyWithLinkedComputersOk() throws ServiceException {
        long count = serviceCompany.count();
        long id = serviceCompany.create(NAME_VALID);
        assertTrue(count + 1 == serviceCompany.count());
        long idC = serviceComputer.create(NAME_VALID, null, null, id);
        Assert.assertNotNull(serviceComputer.get(idC));
        serviceCompany.delete(id);
        serviceComputer.get(idC);
    }

    /**
     * Méthode de test du deleteComputer.
     * @throws ServiceException erreur sur le service
     */
    @Test
    public void testDeleteManyCompaniesWithoutLinkedComputersOk() throws ServiceException {
        long count = serviceCompany.count();
        long id = serviceCompany.create(NAME_VALID);
        long id2 = serviceCompany.create(NAME_VALID);
        long id3 = serviceCompany.create(NAME_VALID);
        assertTrue(count + 3 == serviceCompany.count());
        serviceCompany.deleteAll(new HashSet<Long>(Arrays.asList(id, id2, id3)));
        assertTrue(count == serviceCompany.count());
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     */
    @Test(expected = ComputerNotFoundException.class)
    public void testDeleteManyCompaniesWithLinkedComputersOk() throws ServiceException {
        long count = serviceCompany.count();
        long id = serviceCompany.create(NAME_VALID);
        assertTrue(count + 1 == serviceCompany.count());
        long idC = serviceComputer.create(NAME_VALID, null, null, id);
        Assert.assertNotNull(serviceComputer.get(idC));
        serviceCompany.deleteAll(new HashSet<Long>(Arrays.asList(id)));
        serviceComputer.get(idC);
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteManyCompanyWithBadList() throws ServiceException {
        serviceCompany.deleteAll(null);
    }

    /**
     * Méthode de test du deleteCompanies.
     * @throws ServiceException erreur sur le service
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDeleteManyCompanyWithEmptyList() throws ServiceException {
    	serviceCompany.deleteAll(new HashSet<Long>());
    }

}
