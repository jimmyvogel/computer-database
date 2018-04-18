package main.java.com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.List;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.persistence.CompanyDaoImpl;
import main.java.com.excilys.cdb.persistence.ComputerDaoImpl;
import main.java.com.excilys.cdb.persistence.DaoFactory;

public class ComputerServiceImpl implements IComputerService{

	private ComputerDaoImpl computerDao;
	private CompanyDaoImpl companyDao;
	
	public ComputerServiceImpl() {
		DaoFactory factory = new DaoFactory();
		computerDao = factory.getComputerDao();
		companyDao = factory.getCompanyDao();
	}

	@Override
	public List<Computer> getAllComputer() {
		return computerDao.getComputers();
	}

	@Override
	public List<Company> getAllCompany() {
		return companyDao.getCompanies();
	}

	@Override
	public Company getCompany(long id) {
		return companyDao.getCompanyById(id);
	}

	@Override
	public Computer getComputer(long id) {
		return computerDao.getComputerById(id);
	}

	@Override
	public boolean createComputer(String name) {
		if(name == null) return false;
		if(!Computer.validName(name))return false;
		
		String s = validTextProcess(name);
		Computer c = new Computer(s);
		
		return computerDao.createComputer(c);
	}

	@Override
	public boolean createComputer(String name, LocalDateTime introduced, 
			LocalDateTime discontinued, long companyId) {
		if(name==null)
			return false;
		if(!Computer.validName(name))return false;
		
		String s = validTextProcess(name);
		Computer c = new Computer(s);
		
		//Verification validité date.
		if(introduced!=null && !Computer.validDate(introduced))
			return false;
		else
			c.setIntroduced(introduced);
		if(discontinued!=null && !Computer.validDate(discontinued))
			return false;
		else
			c.setDiscontinued(discontinued);
		
		if(introduced!=null && discontinued != null) {
			if(introduced.isAfter(discontinued))return false;
		}
		
		Company inter;
		if(companyId > 0 && (inter = companyDao.getCompanyById(companyId))!=null)
			c.setCompany(inter);
		
		return computerDao.createComputer(c);
	}

	@Override
	public boolean deleteComputer(long id) {
		if(id < 1)
			return false;
		
		Computer c = computerDao.getComputerById(id);
		if(c == null)
			return false;
		
		return computerDao.deleteComputer(c);
	}

	@Override
	public boolean updateComputer(long id, String name) {
		if(name == null) return false;
		if(!Computer.validName(name))return false;
		
		String s = validTextProcess(name);
		
		Computer c = computerDao.getComputerById(id);
		if(c == null)
			return false;
		
		c.setName(s);
		
		return computerDao.updateComputer(c);
	}

	@Override
	public boolean updateComputer(long id, String name, LocalDateTime introduced, LocalDateTime discontinued,
			long companyId) {
		if(name == null && introduced == null && discontinued == null && companyId < 1)
			return false;
		if(name!=null && !Computer.validName(name))return false;
		
		//Verification validité date.
		if(introduced!=null && !Computer.validDate(introduced))
			return false;
		if(discontinued!=null && !Computer.validDate(discontinued))
			return false;

		
		//Recuperation initial.
		Computer initial = computerDao.getComputerById(id);
		
		if(initial == null) return false;
		
		//Initialisation du nouveau computer.
		Computer nouveau = new Computer(initial.getId(), initial.getName(),
				initial.getIntroduced(), initial.getDiscontinued(), initial.getCompany());
		
		if(name != null) {
			String s = validTextProcess(name);
			nouveau.setName(s);
		}
		
		//Gestion des cas ou introduced ou discontinued existe
		if(introduced != null || discontinued != null) {
			
			//Les deux existent
			if(introduced != null && discontinued != null) {
				if(introduced.isAfter(discontinued))return false;
				nouveau.setIntroduced(introduced);
				nouveau.setDiscontinued(discontinued);
			}else {
				//Seulement introduced est present
				if(introduced != null) {
					
					if(initial.getDiscontinued()!=null 
							&& introduced.isAfter(initial.getDiscontinued()))return false;
					
					//Pas de problème temporel si le discontinued n'existait pas.
					nouveau.setIntroduced(introduced);
				}else {
					//Discontinued ne peut exister si il n'y en avait pas avant.
					if(initial.getIntroduced()==null)return false;
					if(discontinued.isBefore(initial.getIntroduced()))return false;
				}
			}
		}
		
		//Gestion différente selon si on a déjà une company lié ou non.
		if(companyId > 0) {
			Company comp = companyDao.getCompanyById(companyId);
			if(comp==null)return false;
			nouveau.setCompany(comp);
		}
		
		return computerDao.updateComputer(nouveau);
	}
	
	@Override
	public long countComputers() {
		return computerDao.getComputerCount();
	}
	
	@Override
	public long countCompanies() {
		return companyDao.getCompanyCount();
	}
	
	public String validTextProcess(String s) {
		return s.replaceAll("<[^>]*>", "");
	}
}
