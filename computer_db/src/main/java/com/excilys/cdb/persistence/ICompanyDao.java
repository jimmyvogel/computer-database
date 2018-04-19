package main.java.com.excilys.cdb.persistence;

import java.util.List;

import main.java.com.excilys.cdb.model.Company;

/**
 * Interface représentant les méthodes à implémenter pour une compagnie dao.
 * @author vogel
 *
 */
public interface ICompanyDao{

	/**
	 * Méthode pour récupérer toutes les compagnies.
	 * @return List type des compagnies.
	 */
	public List<Company> getCompanies();
	
	/**
	 * Méthode pour récupére une compagnie par son id.
	 * @param id l'id de la compagnie demandé
	 * @return an object of type Company.
	 */
	public Company getCompanyById(long id);
	
	/**
	 * Retour le nombre de compagnies existant en base de donnée.
	 * @return a result in long format.
	 */
	public long getCompanyCount();
	
}
