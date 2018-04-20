package com.excilys.cdb.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Classe gérant le mapping des résultats sql sous un format objets.
 * @author vogel
 *
 */
public class MapperDao {

	/**
	 * Mapping d'une ligne de la table company en un objet company
	 * @param result an object du type ResultSet
	 * @return an object du type Company
	 */
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
	
	/**
	 * Mapping d'une ligne de la table computer en un objet computer
	 * @param result an object du type ResultSet
	 * @return an object du type Computer
	 */
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
