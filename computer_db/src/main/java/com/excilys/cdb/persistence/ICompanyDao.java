package main.java.com.excilys.cdb.persistence;

import java.util.List;

import main.java.com.excilys.cdb.model.Company;

public interface ICompanyDao {

	
	public List<Company> getCompanies();
	public Company getCompanyById(long id);
	public long getCompanyCount();
	
}
