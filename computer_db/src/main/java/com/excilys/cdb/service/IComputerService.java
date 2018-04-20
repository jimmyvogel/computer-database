package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Page;

/**
 * Interface représentant les méthodes du service de gestion des computers nécessaires.
 * @author vogel
 *
 */
public interface IComputerService {

	/**
	 * Optenir tous les computers.
	 * @return un objet de type List
	 */
	public List<Computer> getAllComputer();
	
	/**
	 * Optenir toutes les compagnies.
	 * @return un objet de type List
	 */
	public List<Company> getAllCompany();
	
	public Page<Company> getPageCompany(int page);
	
	public Page<Computer> getPageComputer(int page);
	
	/**
	 * Optenir une compagnie.
	 * @param id l'id de la compagnie demandé.
	 * @return an objet of type Company or null.
	 */
	public Company getCompany(long id);
	
	/**
	 * Optenir un computer.
	 * @param id l'id du computer demandé.
	 * @return an objet of type Computer or null.
	 */
	public Computer getComputer(long id);
	
	/**
	 * Sauvegarder un nouveau computer avec juste un nom spécifié.
	 * @param name le nom du nouveau computer
	 * @return l'id de l'objet pour spécifié la réussite ou -1.
	 */
	public long createComputer(String name);
	
	/**
	 * Sauvegarder un nouveau computer avec tous les champs spécifiés
	 * @param name le nom du nouveau computer
	 * @param introduced la date de type LocalDateTime d'introduction
	 * @param discontinued la date de type LocalDateTime d'arrêt
	 * @param companyId l'id de la compagnie de manufacture du computer
	 * @return l'id de l'objet pour spécifié la réussite ou -1.
	 */
	public long createComputer(String name, LocalDateTime introduced, 
			LocalDateTime discontinued, long companyId);
	
	/**
	 * Modifier un computer avec juste un nom spécifié.
	 * @param id l'id du computer a modifié.
	 * @param name le nouveau nom du computer
	 * @return un boolean pour spécifié la réussite ou non de la modification.
	 */
	public boolean updateComputer(long id, String name);
	
	/**
	 * Modifier un  computer avec tous les champs spécifiés ou non (un nécessaire)
	 * @param name le nouveau nom du computer
	 * @param introduced la nouvelle date de type LocalDateTime d'introduction
	 * @param discontinued la nouvelle date de type LocalDateTime d'arrêt
	 * @param companyId le nouvel id de la compagnie de manufacture du computer
	 * @return un boolean pour spécifié la réussite ou non de la modification.
	 */
	public boolean updateComputer(long id, String name, LocalDateTime introduced, 
			LocalDateTime discontinued, long companyId);
	
	/**
	 * Supprimer un computer avec juste un id spécifié.
	 * @param id l'id du computer a supprimé.
	 * @return un boolean pour spécifié la réussite ou non de la suppression.
	 */
	public boolean deleteComputer(long id);

	/**
	 * Return le nombre de computers existants.
	 * @return un nombre de type long.
	 */
	public long countComputers();
	
	/**
	 * Return le nombre de company existants.
	 * @return un nombre de type long.
	 */
	public long countCompanies();
}
