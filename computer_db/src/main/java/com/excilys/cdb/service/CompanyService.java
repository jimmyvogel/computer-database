package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyCrudDao;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.CompanyNotFoundException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servlet.ressources.DefaultValues;
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
	private CompanyCrudDao companyDao;

	/**
	 * Retourne toutes les company de la bdd.
	 * @return une liste
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	public List<Company> getAll() throws ServiceException {
		List<Company> list = new ArrayList<Company>();
		//list = companyDao.getAll();
		list = StreamSupport.stream(companyDao.findAll().spliterator(), false).collect(Collectors.toList());
		return list;
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
	@Transactional(readOnly = true)
	public Page<Company> getPage(final int page, final Integer limit) throws ServiceException {
		Page<Company> pageCompany = null;
		org.springframework.data.domain.Page<Company> qpage;
		if (limit == null) {
			qpage = companyDao.findAll(new QPageRequest(page, DefaultValues.DEFAULT_LIMIT));
		} else {
			qpage = companyDao.findAll(new QPageRequest(page, limit));
		}
		pageCompany = new Page<Company>(limit, qpage.getTotalElements());
		pageCompany.charge(qpage.getContent(), page);
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
	@Transactional(readOnly = true)
	public Page<Company> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		if (search == null) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
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
				throw new ServiceException(e.getMessage());
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
	@Transactional(readOnly = true)
	public Company get(long id) throws ServiceException, DaoException {
		Company company = companyDao.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
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
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		try {
			CompanyValidator.validName(name);
			Company c = new Company(name);
			result = companyDao.save(c).getId();
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(e.getMessage());
		}
		return result;
	}

	/**
	 * Détruire une compagnie.
	 * @param id l'id du computer à supprimer
	 * @return un boolean représentant le résultat
	 * @throws DaoException erreur de requête
	 */
	public void delete(long id) throws DaoException {
		companyDao.deleteById(id);
	}

	/**
	 * Détruire plusieurs compagnies.
	 * @param ids id(s) des compagnies à supprimer
	 * @throws DaoException erreur de requête
	 * @throws ServiceException suppression fail.
	 */
	public void deleteAll(Set<Long> ids) throws DaoException, ServiceException {
		if (ids == null || ids.size() == 0) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.ILLEGAL_ARGUMENTS, null));
		}
		if (!companyDao.deleteAllById(ids)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.DELETE_FAIL, null));
		}
	}

	/**
	 * Récupérer le nombre de compagnies existantes.
	 * @return un type long
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de requête.
	 */
	@Transactional(readOnly = true)
	public long count() throws ServiceException, DaoException {
		return companyDao.count();
	}
}
