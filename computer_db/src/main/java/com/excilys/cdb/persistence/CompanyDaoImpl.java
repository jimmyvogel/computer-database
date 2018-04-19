package main.java.com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.cdb.model.Company;

/**
 * Classe d'implémentation d'une compagnieDao contenant les requêtes possibles sur
 * la table des company de la base de donnée.
 * @author vogel
 *
 */
public class CompanyDaoImpl implements ICompanyDao{

	private DaoFactory factory;
	
	private static final String SQL_ALL_COMPANIES =
			"SELECT `id`,`name` FROM `company`";
	private static final String SQL_ONE_COMPANY =
			"SELECT `id`,`name` FROM `company` WHERE `id`=?";
	private static final String SQL_COUNT_COMPANY =
			"SELECT COUNT(`id`) AS `total` FROM `company`";
	
	/**
	 * Constructor avec la dao en paramètre pour accéder à d'autres daos si nécessaire.
	 * @param factory an object of type DaoFactory
	 */
	public CompanyDaoImpl(DaoFactory factory) {
		super();
		this.factory = factory;
	}

	@Override
	public List<Company> getCompanies() {
		List<Company> companies = new ArrayList<Company>();
		try {
			Connection c = factory.getConnection();
			Statement stmt = c.createStatement();
	        ResultSet result = stmt.executeQuery(SQL_ALL_COMPANIES);
	        
	        /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
	        while(result.next()) {
	            companies.add(MapperDao.mapCompany(result));
	        }
	        
			stmt.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return companies;
	}

	@Override
	public Company getCompanyById(long id) {
		Company company = null;
		try {
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_ONE_COMPANY);
			stmt.setLong(1, id);
			
	        ResultSet result = stmt.executeQuery();
	        
	        /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
	        if(result.next()) {
	        	company = MapperDao.mapCompany(result);
	        }
	        
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return company;
	}

	@Override
	public long getCompanyCount() {
		long count = -1;
		try {
			Connection c = factory.getConnection();
			Statement stmt = c.createStatement();
			String query = SQL_COUNT_COMPANY;
	        ResultSet result = stmt.executeQuery(query);
	        if(result.next())
	        	count = result.getLong("total");
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
	}

}
