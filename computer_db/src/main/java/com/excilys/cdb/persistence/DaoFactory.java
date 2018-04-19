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

	//Les properties en dur pour l'instant //TODO
	
	private static String url;
	private static String username;
	private static String password;
	
	//Variables du fichier properties
	private static final String FICHIER_PROPERTIES = "main/ressources/dao.properties";
	private static final String PROPERTY_URL = "url";
	private static final String PROPERTY_NOM_UTILISATEUR = "username";
	private static final String PROPERTY_MOT_DE_PASSE = "password";
	
	//Variables pour récupérer les id max à l'initialisation pour gérer
	//l'auto-incrémentation en manuelle.
	private static boolean sequenceInitialized=false;
	public static long sequenceComputer;
	public static long sequenceCompany;
	
	//NOT DONE BECAUSE ALL TABLE MUST THEN IMPLEMENTS THE SAME METHODS.
	//Enum pour la généricité de la méthode factory.
	//public static enum DaoType { COMPUTER, COMPANY };
	
	/**
	 * Constructor daofactory
	 */
	public DaoFactory(String url, String username, String password) {
		DaoFactory.url = url;
		DaoFactory.username = username;
		DaoFactory.password = password;
		if(!sequenceInitialized) {
			sequenceInitialized = true;
			sequenceComputer = this.maxIdComputer();
			sequenceCompany = this.maxIdCompany();
		}
	}
	
	/**
	 * Méthode privée à la classe permettant de savoir le dernier id utilisé dans
	 * la table computer de la bdd.
	 * @return a result in long type.
	 */
	private long maxIdComputer() {
		long max = 0;
		try {
			Connection c = this.getConnection();
			Statement stmt = c.createStatement();
			String sql = "SELECT MAX(id) from `computer`";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
			    max = rs.getLong(1);
			}
	        
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max;
	}
	
	/**
	 * Méthode privée à la classe permettant de savoir le dernier id utilisé dans
	 * la table company de la bdd.
	 * @return a result in long type.
	 */
	private long maxIdCompany() {
		long max = 0;
		try {
			Connection c = this.getConnection();
			Statement stmt = c.createStatement();
			String sql = "SELECT MAX(id) from `company`";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
			    max = rs.getLong(1);
			}
	        
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return max;
	}
	
	/**
	 * Retourne une instance de DaoFactory.
	 * @return un objet DaoFactory
	 * @throws DAOConfigurationException envoie une erreur de config si exception.
	 */
	public static DaoFactory getInstance() throws DAOConfigurationException {
		Properties properties = new Properties();
	    String url;
	    String nomUtilisateur;
	    String motDePasse;
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    InputStream fichierProperties = classLoader.getResourceAsStream( FICHIER_PROPERTIES );
	    
	    if ( fichierProperties == null ) {
	        throw new DAOConfigurationException(
	        		"Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
	    }
	    
	    try {
	        properties.load( fichierProperties );
	        url = properties.getProperty( PROPERTY_URL );
	        nomUtilisateur = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
	        motDePasse = properties.getProperty( PROPERTY_MOT_DE_PASSE );
	        
	    } catch ( IOException e ) {
	        throw new DAOConfigurationException(
	        		"Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
	    }
	    DaoFactory instance = new DaoFactory( url, nomUtilisateur, motDePasse );
	    return instance;
    
}
	/**
	 * Retourne une connection à la base de donnée.
	 * @return an object of class Connection
	 * @throws SQLException Retourne une erreur si la connection à la bdd fail.
	 */
	public Connection getConnection() throws SQLException{
		Connection connection = DriverManager.getConnection(url, username, password);
		return connection;
	}
	

	/**
	 * Factory pour dao company
	 * @return un company dao
	 */
	public ICompanyDao getCompanyDao() {
		return new CompanyDaoImpl(this);
	}
	
	/**
	 * Factory pour dao computer
	 * @return un computer dao.
	 */
	public IComputerDao getComputerDao() {
		return new ComputerDaoImpl(this);
	}
	
	/**public Dao<?> getDao(DaoType daoType) {
		Dao<?> res;
		switch(daoType) {
			case COMPANY:res = new CompanyDaoImpl(this);
			case COMPUTER:res = new ComputerDaoImpl(this);
			default: res = new ComputerDaoImpl(this);
		}
		return res;
	}*/
	
}
