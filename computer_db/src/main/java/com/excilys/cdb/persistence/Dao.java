package com.excilys.cdb.persistence;

import java.util.List;

import com.excilys.cdb.persistence.exceptions.DaoException;

/**
 * interface générique, forme le contrat minimum de toutes les daos.
 * @author vogel
 *
 * @param <T>
 */
public interface Dao<T> {

	/**
	 * Retourne le nombre d'instances de l'objets T dans la bdd
	 * @return un résultat en long
	 * @throws DaoException 
	 */
	public long getCount() throws DaoException;

	/**
	 * Retourne un élement d'une table de la bdd.
	 * @param id l'id de l'élément à récupérer
	 * @return l'objet résultant
	 * @throws DaoException 
	 */
	public T getById(long id) throws DaoException;

	/**
	 * Retourne tous les objets d'un certain type de la bdd.
	 * @return
	 * @throws DaoException 
	 */
	public List<T> getAll() throws DaoException;
	
	/**
	 * Récupérer
	 * @param numeroPage
	 * @return
	 */
	public Page<T> getPage(int numeroPage) throws DaoException;
	
}