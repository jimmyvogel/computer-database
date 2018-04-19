package main.java.com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import main.java.com.excilys.cdb.model.Computer;

/**
 *  Classe d'implémentation d'une IComputerDao contenant les requêtes possibles sur
 * la table des computer de la base de donnée.
 * @author vogel
 *
 */
public class ComputerDaoImpl implements IComputerDao{

	private DaoFactory factory;
	private static final String SQL_ALL_COMPUTERS = 
			"SELECT company.id,company.name, computer.id, "
					+ "computer.name, computer.introduced, computer.discontinued FROM company "
					+ "RIGHT JOIN computer ON company.id = computer.company_id";
	private static final String SQL_ONE_COMPUTER = 
			"SELECT company.id,company.name, computer.id, "
					+ "computer.name, computer.introduced, computer.discontinued FROM company "
					+ "RIGHT JOIN computer ON company.id = computer.company_id WHERE computer.id=?";
	private static final String SQL_AJOUT_COMPUTER = 
			"INSERT into `computer` VALUES (?,?,?,?,?)";
	private static final String SQL_UPDATE_COMPUTER =
			"UPDATE computer SET name=?, introduced=?, discontinued=?, "
			+ "company_id=? WHERE id=? ";
	private static final String SQL_DELETE_COMPUTER = 
			"DELETE FROM `computer` WHERE `id`=?";
	private static final String SQL_COUNT_COMPUTER = 
			"SELECT COUNT(`id`) AS `total` FROM `computer`";
	
	/**
	 * Constructor avec la dao en paramètre pour accéder à d'autres daos si nécessaire.
	 * @param factory an object of type DaoFactory
	 */
	public ComputerDaoImpl(DaoFactory factory) {
		super();
		this.factory = factory;
	}

	@Override
	public List<Computer> getComputers() {
		List<Computer> computers = new ArrayList<Computer>();
		try {
			Connection c = factory.getConnection();
			Statement stmt = c.createStatement();
	        ResultSet result = stmt.executeQuery(SQL_ALL_COMPUTERS);
	        
	        /* Parcours de la ligne de données de l'éventuel ResulSet retourné */
	        Computer computer;
	        while(result.next()) {
	        	computers.add(MapperDao.mapComputer(result));
	        }
	        
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return computers;
	}
	
	@Override
	public Computer getComputerById(long id) {
		Computer computer = null;
		try {
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_ONE_COMPUTER);
			stmt.setLong(1, id);
			
	        ResultSet result = stmt.executeQuery();
	        
	        if(result.next())
	        	computer = MapperDao.mapComputer(result);
	        
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return computer;
	}

	@Override
	public boolean createComputer(Computer computer) {
		int result = 0;
		try {
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_AJOUT_COMPUTER);
			
			//Selection de l'identifiant supérieur à la séquence dans DaoFactory
			stmt.setLong(1, DaoFactory.sequenceComputer+1);
			
			stmt.setString(2, computer.getName());
			if(computer.getIntroduced()!=null)
				stmt.setTimestamp(3, Timestamp.valueOf(computer.getIntroduced()));
			else
				stmt.setTimestamp(3, null);
			
			if(computer.getDiscontinued()!=null)
				stmt.setTimestamp(4, Timestamp.valueOf(computer.getDiscontinued()));
			else
				stmt.setTimestamp(4, null);
			
			if(computer.getCompany()!=null)
				stmt.setLong(5, computer.getCompany().getId());
			else
				stmt.setString(5, null);
			
	        result = stmt.executeUpdate();
	        DaoFactory.sequenceComputer++;
	        
			stmt.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result>0)?true:false;
	}

	@Override
	public boolean updateComputer(Computer computer) {
		int result = 0;
		try {
			Computer before = this.getComputerById(computer.getId());
			
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_UPDATE_COMPUTER);
			stmt.setString(1, computer.getName());
			
			if(computer.getIntroduced()!=null)
				stmt.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced()));
			else
				stmt.setTimestamp(2, null);
			
			if(computer.getIntroduced()!=null)
				stmt.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued()));
			else
				stmt.setTimestamp(3, null);
			
			if(computer.getCompany()!=null)
				stmt.setLong(4, computer.getCompany().getId());
			else
				stmt.setString(4, null);
			
			stmt.setLong(5, computer.getId());
			
	        result = stmt.executeUpdate();
	        
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result>0)?true:false;
	}

	@Override
	public boolean deleteComputer(Computer computer) {
		int result = 0;
		try {
			Connection c = factory.getConnection();
			PreparedStatement stmt = c.prepareStatement(SQL_DELETE_COMPUTER);
			stmt.setLong(1, computer.getId());
			
	        result = stmt.executeUpdate();
	        
			stmt.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (result>0)?true:false;
	}

	@Override
	public long getComputerCount() {
		long count = -1;
		try {
			Connection c = factory.getConnection();
			Statement stmt = c.createStatement();
	        ResultSet result = stmt.executeQuery(SQL_COUNT_COMPUTER);
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
