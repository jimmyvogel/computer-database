package com.excilys.cdb.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.persistence.CDBPage;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.ServiceException;

@Service
@Transactional
public interface IService<T> {

	/**
	 * Retourne tous les objets de type T de la bdd.
	 * @return une liste
	 * @throws ServiceException erreur de paramètres
	 */
	List<T> getAll() throws ServiceException;

	/**
	 * Retourne un T de la bdd.
	 * @param id l'id de l'élément à récupérer
	 * @return le T résultant
	 * @throws ServiceException erreur de paramètres
	 */
	T get(long id) throws ServiceException;

	/**
	 * Créer un T.
	 * @param t T a ajouter
	 * @return long l'id de l'élément ou -1 si fail
	 * @throws ServiceException erreur de paramètres.
	 */
	long create(T t) throws ServiceException;

	/**
	 * Détruire plusieurs T.
	 * @param ids id(s) des T à supprimer
	 * @throws ServiceException set en paramètre vide
	 */
	void deleteAll(Set<Long> ids) throws ServiceException;

	/**
	 * Détruire un T.
	 * @param id l'id du T à supprimer
	 * @return un boolean représentant le résultat
	 * @throws DaoException erreur de requête
	 */
	void delete(long id) throws DaoException;

	/**
	 * Récupérer le nombre de T.
	 * @return un type long
	 * @throws ServiceException erreur de service.
	 */
	long count() throws ServiceException;

	/**
	 * Modifié un T.
	 * @param update le T updaté
	 * @return un boolean pour le résultat
	 * @throws ServiceException erreur de paramètres
	 */
	boolean update(T update) throws ServiceException;

	/**
	 * Optenir une page d'objets avec un nombre d'éléments spécifié.
	 * @param page le numero de la page
	 * @param limit le nombre d'objets à instancié, if null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	CDBPage<T> getPage(int page, Integer limit) throws ServiceException;
}
