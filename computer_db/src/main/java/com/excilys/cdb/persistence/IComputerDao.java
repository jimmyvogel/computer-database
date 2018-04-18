package main.java.com.excilys.cdb.persistence;

import java.util.List;

import main.java.com.excilys.cdb.model.Computer;

public interface IComputerDao {

	public List<Computer> getComputers();
	
	public Computer getComputerById(long id);
	
	public boolean createComputer(Computer computer);
	
	public boolean updateComputer(Computer computer);
	
	public boolean deleteComputer(Computer computer);
	
	public long getComputerCount();
}
