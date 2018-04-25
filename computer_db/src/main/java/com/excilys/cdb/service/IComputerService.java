package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.service.exceptions.ServiceException;

/**
 * Interface représentant les méthodes du service de gestion des computers
 * nécessaires.
 * @author vogel
 *
 */
public interface IComputerService {

    /**
     * Optenir tous les computers.
     * @return un objet de type List
     * @throws ServiceException erreur de service
     */
    List<Computer> getAllComputer() throws ServiceException;

    /**
     * Optenir toutes les compagnies.
     * @return un objet de type List
     * @throws ServiceException erreur de service
     */
    List<Company> getAllCompany() throws ServiceException;

    /**
     * Optenir une page de company.
     * @param page le numero de la page
     * @return une page chargé.
     * @throws ServiceException erreur de service
     */
    Page<Company> getPageCompany(int page) throws ServiceException;

    /**
     * Optenir une page de computer.
     * @param page le numero de la page
     * @return une page chargé.
     * @throws ServiceException erreur de service
     */
    Page<Computer> getPageComputer(int page) throws ServiceException;

    /**
     * Optenir une compagnie.
     * @param id
     *            l'id de la compagnie demandé.
     * @return an objet of type Company or null.
     * @throws ServiceException erreur de service
     */
    Company getCompany(long id) throws ServiceException;

    /**
     * Optenir un computer.
     * @param id
     *            l'id du computer demandé.
     * @return an objet of type Computer or null.
     * @throws ServiceException erreur de service
     */
    Computer getComputer(long id) throws ServiceException;

    /**
     * Sauvegarder un nouveau computer avec juste un nom spécifié.
     * @param name
     *            le nom du nouveau computer
     * @return l'id de l'objet pour spécifié la réussite ou -1.
     * @throws ServiceException erreur de service
     */
    long createComputer(String name) throws ServiceException;

    /**
     * Sauvegarder un nouveau computer avec tous les champs spécifiés.
     * @param name
     *            le nom du nouveau computer
     * @param introduced
     *            la date de type LocalDateTime d'introduction
     * @param discontinued
     *            la date de type LocalDateTime d'arrêt
     * @param companyId
     *            l'id de la compagnie de manufacture du computer
     * @return l'id de l'objet pour spécifié la réussite ou -1.
     * @throws ServiceException erreur de service
     */
    long createComputer(String name, LocalDateTime introduced,
            LocalDateTime discontinued, long companyId) throws ServiceException;

    /**
     * Modifier un computer avec juste un nom spécifié.
     * @param id
     *            l'id du computer a modifié.
     * @param name
     *            le nouveau nom du computer
     * @return un boolean pour spécifié la réussite ou non de la modification.
     * @throws ServiceException erreur de service
     */
    boolean updateComputer(long id, String name) throws ServiceException;

    /**
     * Modifier un computer avec tous les champs spécifiés ou non (un
     * nécessaire).
     * @param id l'id du computer a modifié
     * @param name le nouveau nom du computer
     * @param introduced
     *            la nouvelle date de type LocalDateTime d'introduction
     * @param discontinued
     *            la nouvelle date de type LocalDateTime d'arrêt
     * @param companyId
     *            le nouvel id de la compagnie de manufacture du computer
     * @return un boolean pour spécifié la réussite ou non de la modification.
     * @throws ServiceException erreur de service
     */
    boolean updateComputer(long id, String name,
            LocalDateTime introduced, LocalDateTime discontinued,
            long companyId) throws ServiceException;

    /**
     * Supprimer un computer avec juste un id spécifié.
     * @param id
     *            l'id du computer a supprimé.
     * @return un boolean pour spécifié la réussite ou non de la suppression.
     * @throws ServiceException erreur de service
     */
    boolean deleteComputer(long id) throws ServiceException;

    /**
     * Return le nombre de computers existants.
     * @return un nombre de type long.
     * @throws ServiceException erreur de service
     */
    long countComputers() throws ServiceException;

    /**
     * Return le nombre de company existants.
     * @return un nombre de type long.
     * @throws ServiceException erreur de service
     */
    long countCompanies() throws ServiceException;
}
