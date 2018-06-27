package com.excilys.cdb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.dao.DaoOrder;
import com.excilys.cdb.dao.ComputerCrudDao.PageComputerOrder;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.exceptions.ServiceException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié. (dev)Gestion de la validation des
 * formats des arguments via paramètres dans le service. Gestion de la validité
 * des modeles. Gestion de la securité des arguments. Gestion des exceptions de
 * type validation, ou dao.
 * @author vogel
 */
@Service
@Transactional
public interface IComputerService extends IService<Computer> {


	@Transactional(readOnly = true)
	ComputerDTO get(long id) throws ServiceException;
	
	/**
	 * Recherche de computer par nom.
	 * @param search le nom a chercher
	 * @param page le numero de la page
	 * @param limit le nombre d'éléments à récupérer, si null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<ComputerDTO> getPageSearch(String search, int page, Integer limit) throws ServiceException;
	
	/**
	 * Recherche de computer par nom avec un ordre précis.
	 * @param search le nom a chercher
	 * @param page le numero de la page
	 * @param limit le nombre d'éléments à récupérer, si null: valeur default.
	 * @param order l'ordre demandé.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<ComputerDTO> getPageSearch(String search, int page, Integer limit, PageComputerOrder order) throws ServiceException;

	/**
	 * Créer un computer.
	 * @param name le nom du computer
	 * @return boolean le résultat
	 * @throws ServiceException erreur de paramètres.
	 */
	long create(String name) throws ServiceException;

	/**
	 * Créer un computer.
	 * @param name le nom de la compagnie ou null
	 * @param introduced la date ou null
	 * @param discontinued la date d'arret ou null
	 * @param companyId l'id de la company ou -1.
	 * @return long l'id de l'élément ou -1 si fail
	 * @throws ServiceException erreur de paramètres.
	 */
	long create(String name, LocalDate introduced, LocalDate discontinued, long companyId) throws ServiceException;

	/**
	 * Modifié un computer.
	 * @param id l'id du computer à modifié.
	 * @param name le nouveau nom de la compagnie ou null
	 * @param introduced la nouvelle date ou null
	 * @param discontinued la nouvelle date d'arret ou null
	 * @param companyId l'id de la nouvelle company, 0 pour supprimer, ou -1 pour
	 *            null.
	 * @return un boolean pour le résultat
	 * @throws ServiceException erreur de paramètres
	 */
	boolean update(long id, String name, LocalDate introduced, LocalDate discontinued, long companyId)
			throws ServiceException;

	/**
	 * Détruire les computers liés au compagnies spécifiés.
	 * @param ids ids des compagnies à délier.
	 * @throws ServiceException erreur de paramètres
	 */
	Long deleteAllByCompanyId(Set<Long> ids) throws ServiceException;
	
	/**
	 * Retourne tous les objets de type T de la bdd.
	 * @return une liste
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	List<ComputerDTO> getAll() throws ServiceException;

	/**
	 * Optenir une page d'objets avec un nombre d'éléments spécifié.
	 * @param page le numero de la page
	 * @param limit le nombre d'objets à instancié, if null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<ComputerDTO> getPage(int page, Integer limit) throws ServiceException;
	
	/**
	 * Optenir une page d'objets avec un nombre d'éléments spécifié dans un order précis.
	 * @param page le numero de la page
	 * @param limit le nombre d'objets à instancié, if null: valeur default.
	 * @param order l'ordre demandé
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	@Transactional(readOnly = true)
	Page<ComputerDTO> getPage(int page, Integer limit, DaoOrder order) throws ServiceException;
	
	/**
	 * Modifié un T.
	 * @param update le T updaté
	 * @return un boolean pour le résultat
	 * @throws ServiceException erreur de paramètres
	 */
	boolean update(ComputerDTO update) throws ServiceException;
	
	/**
	 * Créer un T.
	 * @param t T a ajouter
	 * @return long l'id de l'élément ou -1 si fail
	 * @throws ServiceException erreur de paramètres.
	 */
	long create(ComputerDTO t) throws ServiceException;

}
