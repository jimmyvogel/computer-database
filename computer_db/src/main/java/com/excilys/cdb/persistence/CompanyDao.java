package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Classe d'implémentation d'une compagnieDao contenant les requêtes possibles sur
 * la table des company de la base de donnée.
 * @author vogel
 *
 */
public class CompanyDao implements Dao<Company>{

	private DaoFactory factory;
	private static CompanyDao dao;
	
	private static final String SQL_ALL_COMPANIES =
			"SELECT `id`,`name` FROM `company`";
	private static final String SQL_ONE_COMPANY =
			"SELECT `id`,`name` FROM `company` WHERE `id`=?";
	private static final String SQL_COUNT_COMPANY =
			"SELECT COUNT(`id`) AS `total` FROM `company`";
	private static final String SQL_PAGE_COMPANY = 
			SQL_ALL_COMPANIES + " LIMIT ? OFFSET ?"; 
	private static final int LIMIT_DEFAULT = 10;
	
	public static CompanyDao getInstance(DaoFactory factory) {
		if(dao==null) {
			dao = new CompanyDao();
			dao.factory = factory;
		}
		return dao;
	}
	
	public List<Company> getAll() {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return companies;
	}

	public Company getById(long id) {
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
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return company;
	}

	public long getCount() {
		long count = -1;
		try {
			Connection c = factory.getConnection();
			Statement stmt = c.createStatement();
			String query = SQL_COUNT_COMPANY;
	        ResultSet result = stmt.executeQuery(query);
	        if(result.next())
	        	count = result.getLong("total");
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
	}

	public Page<Company> getPage(int numeroPage) {
		Page<Company> page = new Page<Company>(LIMIT_DEFAULT);
		try {
			List<Company> companies= new ArrayList<Company>();
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_PAGE_COMPANY);
			stmt.setInt(1, page.getLimit());
			stmt.setInt(2, page.offset(numeroPage));
			
	        ResultSet result = stmt.executeQuery();
	        
	        while(result.next()) {
	        	companies.add(MapperDao.mapCompany(result));
	        }
	        page.charge(companies);
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return page;
	}

}
