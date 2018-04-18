package main.java.com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DaoFactory {

	private static final String URL = "jdbc:mysql://localhost:3306/computer-database-db-test";
	private static final String USERNAME = "admincdb";
	private static final String PASSWORD = "qwerty1234";
	private static final String MAX_POOL = "250"; // set your own limit
	
	private static boolean sequenceInitialized=false;
	public static long sequenceComputer;
	public static long sequenceCompany;
	
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
	
	public CompanyDaoImpl getCompanyDao() {
		return new CompanyDaoImpl(this);
	}
	
	public ComputerDaoImpl getComputerDao() {
		return new ComputerDaoImpl(this);
	}
	
}
