package main.java.com.excilys.cdb.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import main.java.com.excilys.cdb.persistence.exceptions.DAOConfigurationException;

/**
 * Factory pour la création des daos gérant les requêtes sur la base de donnée.
 * @author vogel
 *
 */
public class DaoFactory{
	
	//Variables du fichier properties
	private static final String FICHIER_PROPERTIES = "main/ressources/dao.properties";
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_NOM_UTILISATEUR = "username";
	private static final String PROPERTY_MOT_DE_PASSE = "password";
	
	//Variables pour récupérer les id max à l'initialisation pour gérer
	//l'auto-incrémentation en manuelle.
	private static boolean initialized=false;
	
	//Connexion unique
	private static Connection connection;
	
	//DaoFactory unique
	private static DaoFactory dao;
	
	//Enum pour la généricité de la méthode factory.
	public static enum DaoType { COMPUTER, COMPANY };
	
	private static void initializeFactory() throws DAOConfigurationException{
		String url;
		String username;
		String password;
		
		//Single dao;
		dao = new DaoFactory();
		
		//Initialise la connection unique.
		Properties properties = new Properties();
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );
	    if (fichierProperties == null) {
	        throw new DAOConfigurationException(
	        		"Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
	    }
	    try {
	        properties.load( fichierProperties );
	        url = properties.getProperty( PROPERTY_URL );
	        username = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
	        password = properties.getProperty( PROPERTY_MOT_DE_PASSE );
	        
	    } catch ( IOException e ) {
	        throw new DAOConfigurationException(
	        		"Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
	    }
	    try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
	        throw new DAOConfigurationException("Connection impossible", e );
		}
		
		initialized = true;
	}
	
	/**
	 * Retourne une instance de DaoFactory.
	 * @return un objet DaoFactory
	 * @throws DAOConfigurationException envoie une erreur de config si exception.
	 * @throws SQLException 
	 */
	public static DaoFactory getInstance() throws DAOConfigurationException {
		if(!initialized) {
			initializeFactory();
		}
	    return dao;
	}
	
	/**
	 * Retourne une connection à la base de donnée.
	 * @return an object of class Connection
	 */
	public Connection getConnection(){
		return connection;
	}
	

	/**
	 * Factory pour dao company
	 * @return une dao
	 */
	public Dao<?> getDao(DaoType daoType) {
		Dao<?> res=null;
		switch(daoType) {
			case COMPANY:res = CompanyDao.getInstance(this); break;
			case COMPUTER:res = ComputerDao.getInstance(this); break;
			default:break;
		}
		return res;
	}
	
	@Override
	public void finalize() throws Throwable{
		connection.close();
	}
	
}
