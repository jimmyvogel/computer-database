package main.java.com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Factory pour la création des daos gérant les requêtes sur la base de donnée.
 * @author vogel
 *
 */
public class DaoFactory{

	//Les properties en dur pour l'instant //TODO
	private static final String URL = "jdbc:mysql://localhost:3306/computer-database-db-test";
	private static final String USERNAME = "admincdb";
	private static final String PASSWORD = "qwerty1234";
	private static final String MAX_POOL = "250"; // set your own limit
	
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
	public DaoFactory() {
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
	 * Retourn les propriétés de la connection.
	 * @return an object of class Properties
	 */
	public static Properties getProperties() {
		Properties properties = new Properties();
        properties.setProperty("user", USERNAME);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("MaxPooledStatements", MAX_POOL);
	    return properties;
	}
	
	/**
	 * Retourne une connection à la base de donnée.
	 * @return an object of class Connection
	 * @throws SQLException Retourne une erreur si la connection à la bdd fail.
	 */
	public Connection getConnection() throws SQLException{
		Connection connection = DriverManager.getConnection(URL, getProperties());
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
