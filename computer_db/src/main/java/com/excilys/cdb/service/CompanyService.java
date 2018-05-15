package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.CompanyNotFoundException;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.CompanyValidator;
import com.excilys.cdb.validator.TextValidation;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * @author vogel
 *
 */
public class CompanyService {

    private CompanyDao companyDao;

    private static CompanyService service;

    /**
     * Récupérer une instance de type computerServiceImpl.
     * @return une référence sur le singleton ComputerServiceImpl.
     * @throws DAOConfigurationException erreur de configuration
     */
    public static CompanyService getInstance()
            throws DAOConfigurationException {
        if (service == null) {
            Logger logger = LoggerFactory.getLogger(CompanyService.class);
            logger.info("Initialisation du singleton computer service");

            service = new CompanyService();
            DaoFactory factory = DaoFactory.getInstance();
            service.companyDao = (CompanyDao) factory
                    .getDao(DaoFactory.DaoType.COMPANY);
        }
        return service;
    }

    /**
     * Retourne toutes les company de la bdd.
     * @return une liste
     * @throws ServiceException erreur de paramètres
     */
    public List<Company> getAllCompany() throws ServiceException {
        List<Company> list = new ArrayList<Company>();
        try {
            list = companyDao.getAll();
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return list;
    }

    /**
     * Retourne toutes les companies comprenant name.
     * @param name le nom de la(les) company(ies).
     * @return une liste de company
     * @throws ServiceException erreur de paramètres
     * @throws DaoException erreur de reqûete.
     */
    public List<Company> getCompanyByName(String name) throws ServiceException, DaoException {
        String nameTraiter = TextValidation.traitementString(name);
        return companyDao.getByName(nameTraiter);
    }

    /**
     * Optenir une page de company.
     * @param page le numero de la page
     * @return une page chargé.
     * @throws ServiceException erreur de service
     */
    public Page<Company> getPageCompany(final int page) throws ServiceException {
        return getPageCompany(page, null);
    }

    /**
     * Optenir une page de company avec un nombre d'éléments spécifié.
     * @param page le numero de la page
     * @param limit le nombre d'objets à instancié, if null: valeur default.
     * @return une page chargé.
     * @throws ServiceException erreur de paramètres
     */
    public Page<Company> getPageCompany(final int page, final Integer limit)
            throws ServiceException {
        Page<Company> pageCompany = null;
        try {
            if (limit == null) {
                pageCompany = companyDao.getPage(page);
            } else {
                pageCompany = companyDao.getPage(page, limit);
            }
        } catch (DaoException e) {
            throw new ServiceException("Méthode dao fail", e);
        }
        return pageCompany;
    }

    /**
     * Recherche de compagnie par nom.
     * @param search le nom à chercher.
     * @param page le numero de la page
     * @param limit le nombre d'éléments à récupérer, si null: valeur default.
     * @return une page chargé.
     * @throws ServiceException erreur de paramètres
     */
    public Page<Company> getPageSearchCompany(final String search, final int page, final Integer limit) throws ServiceException {
        Page<Company> pageComputer = new Page<Company>(0, 0);
        String searchTraiter = TextValidation.traitementString(search);
        if (search != null) {
            try {
                if (limit == null) {
                    pageComputer = companyDao.getPageSearch(searchTraiter, page);
                } else {
                    pageComputer = companyDao.getPageSearch(searchTraiter, page, limit);
                }
            } catch (DaoException e) {
                throw new ServiceException("Méthode dao fail", e);
            }
        }
        return pageComputer;
    }

    /**
     * Retourne une company de la bdd.
     * @param id l'id de l'élément à récupérer
     * @return la Company résultant
     * @throws ServiceException erreur de paramètres
     * @throws DaoException erreur de reqûete.
     */
    public Company getCompany(long id) throws ServiceException, DaoException {
        Company company = companyDao.getById(id).orElseThrow(() -> new CompanyNotFoundException(id));
        return company;
    }

    /** Créer une company.
     * @param name le nom de la compagnie
     * @return boolean le résultat
     * @throws ServiceException erreur de paramètres.
     * @throws DaoException erreur de requête.
     */
    public long createCompany(final String name) throws ServiceException, DaoException {
        long result = -1;

        String nameTraiter = TextValidation.traitementString(name);
        try {
            CompanyValidator.validName(nameTraiter);
            Company c = new Company(nameTraiter);
            result = companyDao.create(c);
        } catch (ValidatorStringException e) {
            throw new NameInvalidException(nameTraiter, e.getReason());
        }
        return result;
    }

    /** Détruire une compagnie.
     * @param id l'id du computer à supprimer
     * @return un boolean représentant le résultat
     * @throws DaoException erreur de requête
     */
    public boolean deleteCompany(long id) throws DaoException {
        if (id < 1) {
            return false;
        }
        return companyDao.delete(id);
    }

    /** Détruire plusieurs compagnies.
     * @param ids id(s) des compagnies à supprimer
     * @return un boolean représentant le résultat
     * @throws DaoException erreur de requête
     */
    public boolean deleteCompanies(Set<Long> ids) throws DaoException {
        if (ids == null || ids.size() == 0) {
            return false;
        }
        return companyDao.delete(ids);
    }

    /** Récupérer le nombre de compagnies existantes.
     * @return un type long
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de requête.
     */
    public long countCompanies() throws ServiceException, DaoException {
        return companyDao.getCount();
    }
}
