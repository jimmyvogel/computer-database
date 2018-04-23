package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.DaoFactory;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.ServiceException;

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
			Logger logger = LoggerFactory.getLogger(ComputerServiceImpl.class);
			logger.info("Initialisation du singleton computer service");

			service = new ComputerServiceImpl();
			DaoFactory factory = DaoFactory.getInstance();
			service.computerDao = (ComputerDao) factory.getDao(DaoFactory.DaoType.COMPUTER);
			service.companyDao = (CompanyDao) factory.getDao(DaoFactory.DaoType.COMPANY);
		}
		return service;
	}

	public List<Computer> getAllComputer() throws ServiceException {
		List<Computer> list = new ArrayList<Computer>();
		try {
			list = computerDao.getAll();
		} catch (DaoException e) {
			throw new ServiceException("Méthode dao fail", e);
		}
		return list;
	}

	public List<Company> getAllCompany() throws ServiceException {
		List<Company> list = new ArrayList<Company>();
		try {
			list = companyDao.getAll();
		} catch (DaoException e) {
			throw new ServiceException("Méthode dao fail", e);
		}
		return list;
	}
	
	public Page<Company> getPageCompany(int page) throws ServiceException {
		Page<Company> pageCompany = null;
		try {
			pageCompany = companyDao.getPage(page);
		} catch (DaoException e) {
			throw new ServiceException("Méthode dao fail", e);
		}
		return pageCompany;
	}

	public Page<Computer> getPageComputer(int page) throws ServiceException {
		Page<Computer> pageComputer = null;
		try {
			pageComputer = computerDao.getPage(page);
		} catch (DaoException e) {
			throw new ServiceException("Méthode dao fail", e);
		}
		return pageComputer;
	}

	public Company getCompany(long id) throws ServiceException {
		Company company = null;
		try {
			company = companyDao.getById(id);
		} catch (DaoException e) {
			throw new ServiceException("Méthode dao fail", e);
		}
		return company;
	}

	public Computer getComputer(long id) throws ServiceException {
		Computer computer = null;
		try {
			computer = computerDao.getById(id);
		} catch (DaoException e) {
			throw new ServiceException("Méthode dao fail", e);
		}
		return computer;
	}

	public long createComputer(String name) throws ServiceException {
		long result = -1;
		
		if(name != null && Computer.validName(name)){
			String s = validTextProcess(name);
			Computer c = new Computer(s);
			try {
				result = computerDao.create(c);
			} catch (DaoException e) {
				throw new ServiceException("Méthode dao fail", e);
			}
		}
		return result;
	}

	public long createComputer(String name, LocalDateTime introduced, 
			LocalDateTime discontinued, long companyId) throws ServiceException {
		if(name==null)
			return -1;
		
		long result = -1;
		
		if(Computer.validName(name)) {
		
			String s = validTextProcess(name);
			Computer c = new Computer(s);
			
			//Verification validité date, null est accepté.
			if(introduced==null || Computer.validDate(introduced))
				c.setIntroduced(introduced);
			
			if(discontinued==null || Computer.validDate(discontinued))
				c.setDiscontinued(discontinued);
			
			//Soit aucun des deux, soit les deux avec verif sur l'écart, soit juste introduced.
			if(discontinued==null 
					|| (introduced == null&&discontinued==null) 
					|| (introduced != null&&discontinued!=null&&introduced.isBefore(discontinued))) {
				
				try {
					Company inter = companyDao.getById(companyId);
					if(companyId > 0 && (inter = companyDao.getById(companyId))!=null)
						c.setCompany(inter);
				}catch(DaoException e) {
					throw new ServiceException("Méthode dao fail on getById", e);
				}
				
				try {
					result = computerDao.create(c);
				}catch(DaoException e) {
					throw new ServiceException("Méthode dao fail", e);
				}
			}
			
		}
		return result;
	}

	public boolean deleteComputer(long id) throws ServiceException {
		if(id < 1)
			return false;
		
		boolean result = false;
		try {
			result = computerDao.deleteComputer(id);
		} catch (DaoException e) {
			throw new ServiceException("Méthode de dao fail", e);
		}
		return result;
	}

	public boolean updateComputer(long id, String name) throws ServiceException {
		
		boolean result = false;
		if(name != null && Computer.validName(name)) {
			String s = validTextProcess(name);
			Computer c;
			try {
				c = computerDao.getById(id);
			} catch (DaoException e) {
				throw new ServiceException("Méthode de la dao getbyid fail", e);
			}
			if(c != null) {
				c.setName(s);
				try {
					result = computerDao.update(c);
				} catch (DaoException e) {
					throw new ServiceException("Méthode de la dao update fail", e);
				}
			}
		}
		
		return result;
	}

	public boolean updateComputer(long id, String name, LocalDateTime introduced, LocalDateTime discontinued,
			long companyId) throws ServiceException {
		if(name == null && introduced == null && discontinued == null && companyId < 1)
			return false;
		if(name!=null && !Computer.validName(name))return false;
		
		//Verification validité date.
		if(introduced!=null && !Computer.validDate(introduced))
			return false;
		if(discontinued!=null && !Computer.validDate(discontinued))
			return false;

		boolean result = false;
		try {
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
		
			result = computerDao.update(nouveau);
		}catch(DaoException e) {
			throw new ServiceException("Méthode de la dao update fail", e);
		}
		
		return result;
	}
	
	public long countComputers() throws ServiceException {
		long count = 0;
		try {
			count = computerDao.getCount();
		} catch (DaoException e) {
			throw new ServiceException("Méthode de la dao fail", e);
		}
		return count;
	}
	
	public long countCompanies() throws ServiceException {
		long count = 0;
		try {
			count = companyDao.getCount();
		} catch (DaoException e) {
			throw new ServiceException("Méthode de la dao fail", e);
		}
		return count;
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
