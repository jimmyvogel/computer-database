package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.CompanyNotFoundException;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.CompanyValidator;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * (dev)Gestion de la validation des formats des arguments via paramètres dans le service.
 * 		Gestion de la validité des références.
 * 	    Gestion de la securité des arguments.
 * 	    Gestion des exceptions de type validation, ou dao.
 * @author vogel
 *
 */
@Service
@Transactional
public class CompanyService {

	@Autowired
	private CompanyDao companyDao;

	/**
	 * Récupérer une instance de type computerServiceImpl.
	 * @return une référence sur le singleton ComputerServiceImpl.
	 * @throws DaoConfigurationException erreur de configuration
	 */
	public CompanyService getInstance() throws DaoConfigurationException {
		Logger logger = LoggerFactory.getLogger(CompanyService.class);
		logger.info("Initialisation du singleton computer service");
		CompanyService service = new CompanyService();
		return service;
	}

	/**
	 * Retourne toutes les company de la bdd.
	 * @return une liste
	 * @throws ServiceException erreur de paramètres
	 */
	public List<Company> getAll() throws ServiceException {
		List<Company> list = new ArrayList<Company>();
		list = companyDao.getAll();
		return list;
	}

	/**
	 * Retourne toutes les companies comprenant name.
	 * @param name le nom de la(les) company(ies).
	 * @return une liste de company
	 * @throws ServiceException erreur de paramètres
	 * @throws DaoException erreur de reqûete.
	 */
	public List<Company> getByName(String name) throws ServiceException, DaoException {
		if (name == null || name.isEmpty()) {
			throw new ServiceException("La recherche ne peut pas être null ou empty");
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException("La recherche contient des characters spéciaux illégaux.");
		}
		return companyDao.getByName(name);
	}

	/**
	 * Optenir une page de company.
	 * @param page le numero de la page
	 * @return une page chargé.
	 * @throws ServiceException erreur de service
	 */
	public Page<Company> getPage(final int page) throws ServiceException {
		return getPage(page, null);
	}

	/**
	 * Optenir une page de company avec un nombre d'éléments spécifié.
	 * @param page le numero de la page
	 * @param limit le nombre d'objets à instancié, if null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	public Page<Company> getPage(final int page, final Integer limit) throws ServiceException {
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
	public Page<Company> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		if (search == null) {
			throw new ServiceException("Le nom ne peut pas être null ou empty");
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new ServiceException("La recherche contient des characters spéciaux illégaux.");
		}
		Page<Company> pageComputer = new Page<Company>(0, 0);
		if (search != null) {
			try {
				if (limit == null) {
					pageComputer = companyDao.getPageSearch(search, page);
				} else {
					pageComputer = companyDao.getPageSearch(search, page, limit);
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
	public Company get(long id) throws ServiceException, DaoException {
		Company company = companyDao.getById(id).orElseThrow(() -> new CompanyNotFoundException(id));
		return company;
	}

	/**
	 * Créer une company.
	 * @param name le nom de la compagnie
	 * @return boolean le résultat
	 * @throws ServiceException erreur de paramètres.
	 * @throws DaoException erreur de requête.
	 */
	public long create(final String name) throws ServiceException, DaoException {
		long result = -1;
		if (name == null || name.isEmpty()) {
			throw new ServiceException("Le nom ne peut pas être null ou empty");
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException("Le nom ne pas pas contenir des characters spéciaux illégaux.");
		}
		try {
			CompanyValidator.validName(name);
			Company c = new Company(name);
			result = companyDao.create(c);
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(name, e.getMessage());
		}
		return result;
	}

	/**
	 * Détruire une compagnie.
	 * @param id l'id du computer à supprimer
	 * @return un boolean représentant le résultat
	 * @throws DaoException erreur de requête
	 */
	public boolean delete(long id) throws DaoException {
		return companyDao.delete(id);
	}

	/**
	 * Détruire plusieurs compagnies.
	 * @param ids id(s) des compagnies à supprimer
	 * @throws DaoException erreur de requête
	 * @throws ServiceException suppression fail.
	 */
	public void deleteAll(Set<Long> ids) throws DaoException, ServiceException {
		if (ids == null || ids.size() == 0) {
			throw new ServiceException("Aucun éléments selectionnés");
		}
		if (!companyDao.deleteAll(ids)) {
			throw new ServiceException("La suppression n'a pas abouti.");
		}
	}

	/**
	 * Récupérer le nombre de compagnies existantes.
	 * @return un type long
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de requête.
	 */
	public long count() throws ServiceException, DaoException {
		return companyDao.getCount();
	}
}
