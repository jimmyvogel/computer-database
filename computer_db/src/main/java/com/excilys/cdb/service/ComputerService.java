package com.excilys.cdb.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.ComputerDao;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.CompanyNotFoundException;
import com.excilys.cdb.persistence.exceptions.ComputerNotFoundException;
import com.excilys.cdb.persistence.exceptions.DaoConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.DateInvalidException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.ComputerValidator;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.excilys.cdb.validator.exceptions.ValidatorDateException;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * (dev)Gestion de la validation des formats des arguments via paramètres dans le service.
 * 		Gestion de la validité des modeles.
 * 	    Gestion de la securité des arguments.
 * 	    Gestion des exceptions de type validation, ou dao.
 * @author vogel
 */
@Service
@Transactional
public class ComputerService {

	@Autowired
	private ComputerDao computerDao;
	@Autowired
	private CompanyDao companyDao;

	/**
	 * Récupérer une instance de type computerServiceImpl.
	 * @return une référence sur le singleton ComputerServiceImpl.
	 * @throws DaoConfigurationException erreur de configuration
	 */
	public static ComputerService getInstance() throws DaoConfigurationException {
		Logger logger = LoggerFactory.getLogger(ComputerService.class);
		logger.info("Initialisation du singleton computer service");

		ComputerService service = new ComputerService();
		return service;
	}

	/**
	 * Retourne tous les computers de la bdd.
	 * @return une liste
	 * @throws ServiceException erreur de paramètres
	 */
	public List<Computer> getAll() throws ServiceException {
		List<Computer> list = new ArrayList<Computer>();
		list = computerDao.getAll();
		return list;
	}

	/**
	 * Retourne tous les computer de nom : name.
	 * @param name le nom du(des) computer(s).
	 * @return une liste de computers
	 * @throws ServiceException erreur de paramètres
	 * @throws DaoException erreur de reqûete.
	 */
	public List<Computer> getByName(String name) throws ServiceException, DaoException {
		if (name == null || name.isEmpty()) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		return computerDao.getByName(name);
	}

	/**
	 * Optenir une page de computer avec un nombre d'éléments spécifié.
	 * @param page le numero de la page
	 * @param limit le nombre d'éléments à récupérer, si null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	public Page<Computer> getPage(final int page, final Integer limit) throws ServiceException {
		Page<Computer> pageComputer = null;
		try {
			if (limit == null) {
				pageComputer = computerDao.getPage(page);
			} else {
				pageComputer = computerDao.getPage(page, limit);
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
		return pageComputer;
	}

	/**
	 * Recherche de computer par nom.
	 * @param search le nom a chercher
	 * @param page le numero de la page
	 * @param limit le nombre d'éléments à récupérer, si null: valeur default.
	 * @return une page chargé.
	 * @throws ServiceException erreur de paramètres
	 */
	public Page<Computer> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		if (search == null) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		Page<Computer> pageComputer = null;
		try {
			if (limit == null) {
				pageComputer = computerDao.getPageSearch(search, page);
			} else {
				pageComputer = computerDao.getPageSearch(search, page, limit);
			}
		} catch (DaoException e) {
			throw new ServiceException(e.getMessage());
		}
		return pageComputer;
	}

	/**
	 * Retourne un computer de la bdd.
	 * @param id l'id de l'élément à récupérer
	 * @return le Computer résultant
	 * @throws ServiceException erreur de paramètres
	 * @throws DaoException erreur de reqûete.
	 */
	public Computer get(long id) throws ServiceException, DaoException {
		Computer computer = computerDao.getById(id).orElseThrow(() -> new ComputerNotFoundException(id));
		return computer;
	}

	/**
	 * Créer un computer.
	 * @param name le nom du computer
	 * @return boolean le résultat
	 * @throws ServiceException erreur de paramètres.
	 * @throws DaoException erreur de requête.
	 */
	public long create(final String name) throws ServiceException, DaoException {
		long result = -1;
		if (name == null) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		try {
			ComputerValidator.validName(name);
			result = computerDao.create(new Computer(name));
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(e.getMessage());
		}
		return result;
	}

	/**
	 * Créer un computer.
	 * @param computer computer a ajouter
	 * @return long l'id de l'élément ou -1 si fail
	 * @throws ServiceException erreur de paramètres.
	 * @throws DaoException erreur de requête.
	 */
	public long create(final Computer computer) throws ServiceException, DaoException {
		if (computer == null) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.ILLEGAL_ARGUMENTS, null));
		}
		return create(computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompany().getId());
	}

	/**
	 * Créer un computer.
	 * @param name le nom de la compagnie ou null
	 * @param introduced la date ou null
	 * @param discontinued la date d'arret ou null
	 * @param companyId l'id de la company ou -1.
	 * @return long l'id de l'élément ou -1 si fail
	 * @throws ServiceException erreur de paramètres.
	 * @throws DaoException erreur de requête.
	 */
	public long create(String name, LocalDateTime introduced, LocalDateTime discontinued, long companyId)
			throws ServiceException, DaoException {
		if (name == null) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		long result = -1;
		try {
			Company inter = null;
			if (companyId > 0) {
				inter = companyDao.getById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
			}
			Computer c = new Computer(name, introduced, discontinued, inter);
			ComputerValidator.validComputer(c);
			result = computerDao.create(c);
		} catch (ValidatorDateException e) {
			throw new DateInvalidException(e.getMessage());
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(e.getMessage());
		}

		return result;
	}

	/**
	 * Détruire un computer.
	 * @param id l'id du computer à supprimer
	 * @return un boolean représentant le résultat
	 * @throws DaoException erreur de requête
	 */
	public boolean delete(long id) throws DaoException {
		return computerDao.delete(id);
	}

	/**
	 * Détruire plusieurs computers.
	 * @param ids id(s) des computers à supprimer
	 * @throws DaoException erreur de requête
	 * @throws ServiceException set en paramètre vide
	 */
	public void deleteAll(Set<Long> ids) throws DaoException, ServiceException {
		if (ids == null || ids.size() == 0) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.ILLEGAL_ARGUMENTS, null));
		}
		if (!computerDao.delete(ids)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.DELETE_FAIL, null));
		}
	}

	/**
	 * Modifié un computer.
	 * @param id l'id du computer a modifié
	 * @param name le nouveau nom du computer
	 * @return boolean le résultat
	 * @throws DaoException erreur de reqûete.
	 * @throws ServiceException erreur de paramètres.
	 */
	public boolean update(long id, String name) throws ServiceException, DaoException {
		if (name == null || name.isEmpty()) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		Computer c;
		try {
			ComputerValidator.validName(name);
			c = computerDao.getById(id).orElseThrow(() -> new ComputerNotFoundException(id));
			c.setName(name);
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(e.getMessage());
		}
		return computerDao.update(c);
	}

	/**
	 * Modifié un computer.
	 * @param update le computer updaté
	 * @return un boolean pour le résultat
	 * @throws ServiceException erreur de paramètres
	 * @throws DaoException erreur de reqûete.
	 */
	public boolean update(Computer update) throws ServiceException, DaoException {
		if (update == null) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		return this.update(update.getId(), update.getName(), update.getIntroduced(), update.getDiscontinued(), update.getCompany().getId());
	}

	/**
	 * Modifié un computer.
	 * @param id l'id du computer à modifié.
	 * @param name le nouveau nom de la compagnie ou null
	 * @param introduced la nouvelle date ou null
	 * @param discontinued la nouvelle date d'arret ou null
	 * @param companyId l'id de la nouvelle company, 0 pour supprimer, ou -1 pour
	 *            null.
	 * @return un boolean pour le résultat
	 * @throws ServiceException erreur de paramètres
	 * @throws DaoException erreur de reqûete.
	 */
	public boolean update(long id, String name, LocalDateTime introduced, LocalDateTime discontinued,
			long companyId) throws ServiceException, DaoException {
		if (name == null && introduced == null && discontinued == null && companyId == -1) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.ILLEGAL_ARGUMENTS, null));
		}
		if (name != null && !SecurityTextValidation.valideString(name)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		Computer nouveau = new Computer();
		Computer initial = computerDao.getById(id).orElseThrow(() -> new ComputerNotFoundException(id));
		nouveau.setId(initial.getId());
		nouveau.setName(name == null || name.trim().isEmpty() ? initial.getName() : name);
		nouveau.setIntroduced(introduced == null ? initial.getIntroduced() : introduced);
		nouveau.setDiscontinued(discontinued == null ? initial.getDiscontinued() : discontinued);
		if (companyId == -1) {
			nouveau.setCompany(initial.getCompany());
		} else if (companyId == 0) {
			nouveau.setCompany(null);
		} else {
			Company comp = companyDao.getById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
			nouveau.setCompany(comp);
		}
		try {
			ComputerValidator.validComputer(nouveau);
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(e.getMessage());
		} catch (ValidatorDateException e) {
			throw new DateInvalidException(e.getMessage());
		}

		return computerDao.update(nouveau);
	}

	/**
	 * Récupérer le nombre de computers.
	 * @return un type long
	 * @throws ServiceException erreur de service.
	 * @throws DaoException erreur de requête.
	 */
	public long count() throws ServiceException, DaoException {
		return computerDao.getCount();
	}
}
