package main.java.com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

public class MapperDao {

	public static Company mapCompany(ResultSet result) {
		try {
			//id and name
			if(result.getString(1) != null && result.getString(2)!= null){
				return new Company(result.getLong(1), result.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Computer mapComputer(ResultSet result) {
		Computer computer = new Computer();
		
		try {
			computer.setId(result.getLong(3));
			computer.setName(result.getString(4));
			if(result.getString(5)!=null)
				computer.setIntroduced(result.getTimestamp(5).toLocalDateTime());
			
			if(result.getString(6)!=null)
				computer.setDiscontinued(result.getTimestamp(6).toLocalDateTime());
			
			if(result.getString(2)!=null) {
				Company c = new Company(result.getLong(1), result.getString(2));
				computer.setCompany(c);
			}
			return computer;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return null;
	}
}
