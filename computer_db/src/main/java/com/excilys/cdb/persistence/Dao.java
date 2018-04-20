package com.excilys.cdb.persistence;

import java.util.List;

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
	 */
	public long getCount();

	/**
	 * Retourne un élement d'une table de la bdd.
	 * @param id l'id de l'élément à récupérer
	 * @return l'objet résultant
	 */
	public T getById(long id);

	/**
	 * Retourne tous les objets d'un certain type de la bdd.
	 * @return
	 */
	public List<T> getAll();
	
	/**
	 * Récupérer
	 * @param numeroPage
	 * @return
	 */
	public Page<T> getPage(int numeroPage);
	
}