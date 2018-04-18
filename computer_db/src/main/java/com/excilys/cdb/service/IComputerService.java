package main.java.com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.List;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;

public interface IComputerService {

	public List<Computer> getAllComputer();
	
	public List<Company> getAllCompany();
	
	public Company getCompany(long id);
	
	public Computer getComputer(long id);
	
	public boolean createComputer(String name);
	
	public boolean createComputer(String name, LocalDateTime introduced, 
			LocalDateTime discontinued, long companyId);
	
	public boolean updateComputer(long id, String name);
	
	public boolean updateComputer(long id, String name, LocalDateTime introduced, 
			LocalDateTime discontinued, long companyId);
	
	public boolean deleteComputer(long id);

	public long countComputers();
	
	public long countCompanies();
}
