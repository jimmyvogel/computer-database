package main.java.com.excilys.cdb.persistence;

import java.util.List;

import main.java.com.excilys.cdb.model.Computer;

/**
 * Interface représentant les méthodes à implémenter pour une computer dao.
 * @author vogel
 *
 */
public interface IComputerDao{

	/**
	 * Méthode pour récupérer tous les computers.
	 * @return List type des computers.
	 */
	public List<Computer> getComputers();
	
	/**
	 * Méthode pour récupére un computer par son id.
	 * @param id l'id du computer demandé
	 * @return an object of type Computer.
	 */
	public Computer getComputerById(long id);
	
	/**
	 * Méthode pour save un computer en mémoire.
	 * @param computer Le computer de type Computer a save.
	 * @return un Boolean spécifiant si la sauvegarde à fonctionné.
	 */
	public boolean createComputer(Computer computer);
	
	/**
	 * Méthode pour modifié un computer en mémoire.
	 * @param computer Le computer de type Computer a save.
	 * @return un Boolean spécifiant si la modification a fonctionné.
	 */
	public boolean updateComputer(Computer computer);
	
	/**
	 * Méthode pour supprimer un computer en mémoire.
	 * @param computer Le computer de type Computer a supprimé.
	 * @return un Boolean spécifiant si la suppression a fonctionné.
	 */
	public boolean deleteComputer(Computer computer);
	
	
	/**
	 * Retour le nombre de computer existant en base de donnée.
	 * @return a result in long format.
	 */
	public long getComputerCount();
}
