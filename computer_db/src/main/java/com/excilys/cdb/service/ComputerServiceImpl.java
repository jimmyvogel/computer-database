package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer
 * et de la table company qui lui est lié.
 * @author vogel
 *
 */
public class ComputerServiceImpl implements IComputerService{

	private ComputerDao computerDao;
	private CompanyDao companyDao;
	
	private static ComputerServiceImpl service;

	public static ComputerServiceImpl getInstance() throws DAOConfigurationException{
		if(service == null) {
			service = new ComputerServiceImpl();
			DaoFactory factory = DaoFactory.getInstance();
			service.computerDao = (ComputerDao) factory.getDao(DaoFactory.DaoType.COMPUTER);
			service.companyDao = (CompanyDao) factory.getDao(DaoFactory.DaoType.COMPANY);
		}
		return service;
	}

	public List<Computer> getAllComputer() {
		return computerDao.getAll();
	}

	public List<Company> getAllCompany() {
		return companyDao.getAll();
	}
	
	public Page<Company> getPageCompany(int page) {
		return companyDao.getPage(page);
	}

	public Page<Computer> getPageComputer(int page) {
		return computerDao.getPage(page);
	}

	public Company getCompany(long id) {
		return companyDao.getById(id);
	}

	public Computer getComputer(long id) {
		return computerDao.getById(id);
	}

	public boolean createComputer(String name) {
		if(name == null) return false;
		if(!Computer.validName(name))return false;
		
		String s = validTextProcess(name);
		Computer c = new Computer(s);
		
		return computerDao.create(c);
	}

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
		if(companyId > 0 && (inter = companyDao.getById(companyId))!=null)
			c.setCompany(inter);
		
		return computerDao.create(c);
	}

	public boolean deleteComputer(long id) {
		if(id < 1)
			return false;
		
		Computer c = computerDao.getById(id);
		if(c == null)
			return false;
		
		return ((ComputerDao)computerDao).deleteComputer(c);
	}

	public boolean updateComputer(long id, String name) {
		if(name == null) return false;
		if(!Computer.validName(name))return false;
		
		String s = validTextProcess(name);
		
		Computer c = computerDao.getById(id);
		if(c == null)
			return false;
		
		c.setName(s);
		
		return computerDao.update(c);
	}

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
		Computer initial = computerDao.getById(id);
		
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
			Company comp = companyDao.getById(companyId);
			if(comp==null)return false;
			nouveau.setCompany(comp);
		}
		
		return computerDao.update(nouveau);
	}
	
	public long countComputers() {
		return computerDao.getCount();
	}
	
	public long countCompanies() {
		return companyDao.getCount();
	}
	
	/**
	 * Méthode pour modifier en valide le format d'un string
	 * @param s le string a valider
	 * @return un objet de type String.
	 */
	public String validTextProcess(String s) {
		return s.replaceAll("<[^>]*>", "");
	}
}
