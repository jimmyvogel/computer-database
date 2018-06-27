package com.excilys.cdb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.CompanyCrudDao.PageCompanyOrder;
import com.excilys.cdb.dao.DaoOrder;
import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.exceptions.ServiceException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié. (dev)Gestion de la validation des
 * formats des arguments via paramètres dans le service. Gestion de la validité
 * des références. Gestion de la securité des arguments. Gestion des exceptions
 * de type validation, ou dao.
 * @author vogel
 *
 */
@Service
@Transactional
public interface ICompanyService extends IService<Company> {

	/**
	 * Optenir une page de company.
	 * @param page le numero de la page
	 * @return une page chargé.
	 * @throws ServiceException erreur de service
	 */
	@Transactional(readOnly = true)
	Page<CompanyDTO> getPage(int page) throws ServiceException;

	/**
	 * Recherche de compagnie par nom.
	 * @param search le nom à chercher.
	 * @param page le numero de la page
	 * @param limit le nombre d'éléments à récupérer, si null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<CompanyDTO> getPageSearch(String search, int page, Integer limit) throws ServiceException;
	
	@Transactional(readOnly = true)
	CompanyDTO get(long id) throws ServiceException;
	
	/**
	 * Recherche de compagnie par nom avec un ordre précis.
	 * @param search le nom à chercher.
	 * @param page le numero de la page
	 * @param limit le nombre d'éléments à récupérer, si null: valeur default.
	 * @param order l'ordre demandé
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<CompanyDTO> getPageSearch(String search, int page, Integer limit, PageCompanyOrder o) throws ServiceException;

	/**
	 * Créer une company.
	 * @param name le nom de la compagnie
	 * @return boolean le résultat
	 * @throws ServiceException erreur de paramètres.
	 * @throws DaoException erreur de requête.
	 */
	long create(String name) throws ServiceException;

	/**
	 * Retourne tous les objets de type T de la bdd.
	 * @return une liste
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	List<CompanyDTO> getAll() throws ServiceException;

	/**
	 * Optenir une page d'objets avec un nombre d'éléments spécifié.
	 * @param page le numero de la page
	 * @param limit le nombre d'objets à instancié, if null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<CompanyDTO> getPage(int page, Integer limit) throws ServiceException;
	
	/**
	 * Optenir une page d'objets avec un nombre d'éléments spécifié dans un order précis.
	 * @param page le numero de la page
	 * @param limit le nombre d'objets à instancié, if null: valeur default.
	 * @param order l'ordre demandé
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<CompanyDTO> getPage(int page, Integer limit, DaoOrder order) throws ServiceException;

	List<CompanyDTO> getAllLazy() throws ServiceException;
	
	public Page<CompanyDTO> getPageLazy(final int page) throws ServiceException;

	public Page<CompanyDTO> getPageLazy(final int page, final Integer limit) throws ServiceException;
	
	public Page<CompanyDTO> getPageLazy(final int page, final Integer limit, DaoOrder order) throws ServiceException;
	
	public Page<CompanyDTO> getPageSearchLazy(final String search, final int page, final Integer limit)
			throws ServiceException;
	
	public Page<CompanyDTO> getPageSearchLazy(final String search, final int page, final Integer limit, final PageCompanyOrder order) throws ServiceException;

	CompanyDTO getLazy(long id) throws ServiceException;
	
	/**
	 * Modifié un T.
	 * @param update le T updaté
	 * @return un boolean pour le résultat
	 * @throws ServiceException erreur de paramètres
	 */
	boolean update(CompanyDTO update) throws ServiceException;
	
	/**
	 * Créer un T.
	 * @param t T a ajouter
	 * @return long l'id de l'élément ou -1 si fail
	 * @throws ServiceException erreur de paramètres.
	 */
	long create(CompanyDTO t) throws ServiceException;


}
