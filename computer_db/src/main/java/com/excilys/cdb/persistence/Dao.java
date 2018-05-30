package com.excilys.cdb.persistence;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.persistence.exceptions.DaoException;

/**
 * interface générique, forme le contrat minimum de toutes les daos.
 * @author vogel
 *
 * @param <T>
 */
public interface Dao<T> {

    int LIMIT_DEFAULT = 10;

    /**
     * Retourne le nombre d'instances de l'objets T dans la bdd.
     * @return un résultat en long
     * @throws DaoException exception de requête
     */
    long getCount() throws DaoException;

    /**
     * Retourne un élement d'une table de la bdd.
     * @param id
     *            l'id de l'élément à récupérer
     * @return l'objet résultant
     * @throws DaoException exception de requête
     */
    Optional<T> getById(long id) throws DaoException;

    /**
     * Retourne tous les objets d'un certain type de la bdd.
     * @return une liste
     * @throws DaoException exception de requête
     */
    List<T> getAll() throws DaoException;

    /**
     * Récupérer une page avec les objets chargés.
     * @param numeroPage le numero de la page
     * @return exception de requête
     * @throws DaoException erreur de requête
     */
    CDBPage<T> getPage(int numeroPage) throws DaoException;

}
