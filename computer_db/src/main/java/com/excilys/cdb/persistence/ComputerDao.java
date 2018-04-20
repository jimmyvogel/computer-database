package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Computer;

/**
 *  Classe contenant les requêtes possibles sur
 * la table des computer de la base de donnée.
 * @author vogel
 *
 */
public class ComputerDao implements Dao<Computer>{

	private DaoFactory factory;
	private static ComputerDao dao;
	
	private static final String SQL_ALL_COMPUTERS = 
			"SELECT company.id,company.name, computer.id, "
					+ "computer.name, computer.introduced, computer.discontinued FROM company "
					+ "RIGHT JOIN computer ON company.id = computer.company_id";
	private static final String SQL_ONE_COMPUTER = 
			"SELECT company.id,company.name, computer.id, "
					+ "computer.name, computer.introduced, computer.discontinued FROM company "
					+ "RIGHT JOIN computer ON company.id = computer.company_id WHERE computer.id=?";
	private static final String SQL_AJOUT_COMPUTER = 
			"INSERT into `computer` (name,introduced,discontinued,company_id)"
			+ " VALUES (?,?,?,?)";
	private static final String SQL_UPDATE_COMPUTER =
			"UPDATE computer SET name=?, introduced=?, discontinued=?, "
			+ "company_id=? WHERE id=? ";
	private static final String SQL_DELETE_COMPUTER = 
			"DELETE FROM `computer` WHERE `id`=?";
	private static final String SQL_COUNT_COMPUTER = 
			"SELECT COUNT(`id`) AS `total` FROM `computer`";
	private static final String SQL_PAGE_COMPUTER = 
			SQL_ALL_COMPUTERS + " LIMIT ? OFFSET ?"; 
	private static final int LIMIT_DEFAULT = 10;
	
	
	public static ComputerDao getInstance(DaoFactory daoFactory) {
		if(dao==null) {
			
			Logger logger = LoggerFactory.getLogger(ComputerDao.class);
			logger.info("Initialisation du singleton de type ComputerDao");
			
			dao = new ComputerDao();
			dao.factory = daoFactory;
		}
		return dao;
	}

	public List<Computer> getAll() {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			Connection c = factory.getConnection();
			Statement stmt = c.createStatement();
	        ResultSet result = stmt.executeQuery(SQL_ALL_COMPUTERS);
	        
	        while(result.next()) {
	        	computers.add(MapperDao.mapComputer(result));
	        }
	        
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return computers;
	}
	
	public Computer getById(long id) {
		Computer computer = null;
		try {
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_ONE_COMPUTER);
			stmt.setLong(1, id);
			
	        ResultSet result = stmt.executeQuery();
	        
	        if(result.next())
	        	computer = MapperDao.mapComputer(result);
	        
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return computer;
	}

	/**
	 * Créer un objet de type computer
	 * @param computer Un objet complet en argument
	 * @return 
	 */
	public long create(Computer computer) {
		long id = -1;
		try {
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_AJOUT_COMPUTER,
					Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, computer.getName());
			if(computer.getIntroduced()!=null)
				stmt.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
			else
				stmt.setTimestamp(2, null);
			
			if(computer.getDiscontinued()!=null)
				stmt.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
			else
				stmt.setTimestamp(3, null);
			
			if(computer.getCompany()!=null)
				stmt.setLong(4, computer.getCompany().getId());
			else
				stmt.setString(4, null);
			
			stmt.execute();
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs != null && rs.first())
				id = rs.getLong(1);
	        
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}

	public boolean update(Computer computer) {
		int result = 0;
		try {
			Computer before = this.getById(computer.getId());
			
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_UPDATE_COMPUTER);
			stmt.setString(1, computer.getName());
			
			if(computer.getIntroduced()!=null)
				stmt.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
			else
				stmt.setTimestamp(2, Timestamp.valueOf(before.getIntroduced()));
			
			if(computer.getIntroduced()!=null)
				stmt.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
			else
				stmt.setTimestamp(3, Timestamp.valueOf(before.getDiscontinued()));
			
			if(computer.getCompany()!=null)
				stmt.setLong(4, computer.getCompany().getId());
			else
				if(before.getCompany()!=null)
					stmt.setLong(4, before.getCompany().getId());
				else
					stmt.setString(4, null);
			
			stmt.setLong(5, computer.getId());
			
	        result = stmt.executeUpdate();
	        
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result>0)?true:false;
	}

	public boolean deleteComputer(long id) {
		if(this.getById(id)==null)return false;
		
		int result = 0;
		try {
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_DELETE_COMPUTER);
			stmt.setLong(1, id);
			
	        result = stmt.executeUpdate();
	        
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result>0)?true:false;
	}

	public long getCount() {
		long count = -1;
		try {
			Connection c = factory.getConnection();
			Statement stmt = c.createStatement();
	        ResultSet result = stmt.executeQuery(SQL_COUNT_COMPUTER);
	        if(result.next())
	        	count = result.getLong("total");
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return count;
	}

	public Page<Computer> getPage(int numeroPage) {
		Page<Computer> page = new Page<Computer>(LIMIT_DEFAULT);
		try {
			List<Computer> computers = new ArrayList<Computer>();
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_PAGE_COMPUTER);
			stmt.setInt(1, page.getLimit());
			stmt.setInt(2, page.offset(numeroPage));
			
	        ResultSet result = stmt.executeQuery();
	        
	        while(result.next()) {
	        	computers.add(MapperDao.mapComputer(result));
	        }
	        page.charge(computers);
	        
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return page;
	}

}
