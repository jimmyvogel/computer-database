package com.excilys.cdb.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CDBPage;
import com.excilys.cdb.persistence.exceptions.DaoException;
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
	CDBPage<Company> getPage(int page) throws ServiceException;

	/**
	 * Recherche de compagnie par nom.
	 * @param search le nom à chercher.
	 * @param page le numero de la page
	 * @param limit le nombre d'éléments à récupérer, si null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	CDBPage<Company> getPageSearch(String search, int page, Integer limit) throws ServiceException;

	/**
	 * Créer une company.
	 * @param name le nom de la compagnie
	 * @return boolean le résultat
	 * @throws ServiceException erreur de paramètres.
	 * @throws DaoException erreur de requête.
	 */
	long create(String name) throws ServiceException;

}
