package com.excilys.cdb.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.CDBPage;
import com.excilys.cdb.dao.CompanyCrudDao;
import com.excilys.cdb.dao.ComputerCrudDao;
import com.excilys.cdb.defaultvalues.DefaultValues;
import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.exceptions.CompanyNotFoundException;
import com.excilys.cdb.service.exceptions.ComputerNotFoundException;
import com.excilys.cdb.service.exceptions.DateInvalidException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servicemessage.ServiceMessage;
import com.excilys.cdb.validator.ComputerValidator;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.excilys.cdb.validator.exceptions.ValidatorDateException;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * @author vogel
 */
@Service
public class ComputerService implements IComputerService {

	@Autowired
	private ComputerCrudDao computerDao;
	@Autowired
	private CompanyCrudDao companyDao;

	@Override
	public List<Computer> getAll() throws ServiceException {
		return computerDao.findAll();
	}

	private ComputerService() {
	}

	@Override
	public CDBPage<Computer> getPage(final int page, final Integer limit) throws ServiceException {
		CDBPage<Computer> pageComputer = null;
		Integer nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		Page<Computer> qpage = computerDao.findAll(new QPageRequest(page - 1, nbElements));
		pageComputer = new CDBPage<Computer>(nbElements, qpage.getTotalElements());
		pageComputer.charge(qpage.getContent(), page);
		return pageComputer;
	}

	@Override
	public CDBPage<Computer> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		if (search == null) {
			throw new ServiceException(MessageHandler.getMessage(ServiceMessage.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new ServiceException(MessageHandler.getMessage(ServiceMessage.SPECIAL_CHARACTERS, null));
		}
		CDBPage<Computer> pageComputer = null;
		Integer nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		Page<Computer> qpage = computerDao.findByNameContainingOrCompanyNameContainingOrderByName(search, search,
				new QPageRequest(page - 1, nbElements));
		pageComputer = new CDBPage<Computer>(nbElements, qpage.getTotalElements());
		pageComputer.charge(qpage.getContent(), page);
		return pageComputer;
	}

	@Override
	public Computer get(long id) throws ServiceException {
		Computer computer = computerDao.findById(id).orElseThrow(() -> new ComputerNotFoundException(id));
		return computer;
	}

	@Override
	public long create(final String name) throws ServiceException {
		long result = -1;
		try {
			ComputerValidator.validName(name);
			result = computerDao.save(new Computer(name)).getId();
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(manageStringTypeError(e));
		}
		return result;
	}

	@Override
	public long create(final Computer computer) throws ServiceException {
		if (computer == null) {
			throw new IllegalArgumentException();
		}
		return create(computer.getName(), computer.getIntroduced(), computer.getDiscontinued(),
				computer.getCompany().getId());
	}

	@Override
	public long create(String name, LocalDate introduced, LocalDate discontinued, long companyId)
			throws ServiceException {
		long result = -1;
		try {
			Company inter = null;
			if (companyId > 0) {
				inter = companyDao.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
			}
			Computer c = new Computer(name, introduced, discontinued, inter);
			ComputerValidator.validComputer(c);
			result = computerDao.save(c).getId();
		} catch (ValidatorDateException e) {
			throw new DateInvalidException(manageDateTypeError(e));
		} catch (ValidatorStringException e2) {
			throw new NameInvalidException(manageStringTypeError(e2));
		}

		return result;
	}

	@Override
	public void delete(long id) throws ServiceException {
		computerDao.deleteById(id);
	}

	@Override
	public void deleteAll(Set<Long> ids) throws ServiceException {
		if (ids == null || ids.size() == 0) {
			throw new IllegalArgumentException();
		}
		computerDao.deleteByIdIn(ids);
		// throw new
		// ServiceException(ExceptionHandler.getMessage(MessageException.DELETE_FAIL,
		// null));
	}

	@Override
	public void deleteAllByCompanyId(Set<Long> ids) throws ServiceException {
		computerDao.deleteByCompanyIdIn(ids);
	}

	@Override
	public boolean update(Computer update) throws ServiceException {
		if (update == null) {
			throw new ServiceException(MessageHandler.getMessage(ServiceMessage.VALIDATION_NAME_NULL, null));
		}
		return this.update(update.getId(), update.getName(), update.getIntroduced(), update.getDiscontinued(),
				update.getCompany().getId());
	}

	@Override
	public boolean update(long id, String name, LocalDate introduced, LocalDate discontinued, long companyId)
			throws ServiceException {
		if (name == null && introduced == null && discontinued == null && companyId == -1) {
			throw new IllegalArgumentException();
		}
		Computer nouveau = new Computer();
		Computer initial = computerDao.findById(id).orElseThrow(() -> new ComputerNotFoundException(id));
		nouveau.setId(initial.getId());
		nouveau.setName(name == null || name.trim().isEmpty() ? initial.getName() : name);
		nouveau.setIntroduced(introduced == null ? initial.getIntroduced() : introduced);
		nouveau.setDiscontinued(discontinued == null ? initial.getDiscontinued() : discontinued);
		if (companyId == -1) {
			nouveau.setCompany(initial.getCompany());
		} else if (companyId == 0) {
			nouveau.setCompany(null);
		} else {
			Company comp = companyDao.findById(companyId).orElseThrow(() -> new CompanyNotFoundException(companyId));
			nouveau.setCompany(comp);
		}
		try {
			ComputerValidator.validComputer(nouveau);
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(manageStringTypeError(e));
		} catch (ValidatorDateException e) {
			throw new DateInvalidException(manageDateTypeError(e));
		}

		return computerDao.save(nouveau) != null;
	}

	@Override
	public long count() throws ServiceException {
		return computerDao.count();
	}

	private String manageDateTypeError(ValidatorDateException e) {
		String res = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Computer.PATTERN_DATE);
		Object[] o = Arrays
				.asList(Computer.BEGIN_DATE_VALID.format(formatter), Computer.END_DATE_VALID.format(formatter))
				.toArray();
		switch (e.getTypeError()) {
		case SECOND_DATE_ALONE:
			res = MessageHandler.getMessage(ServiceMessage.COMPUTER_DISCONTINUED_ALONE, null);
			break;
		case ILLEGAL_DATE:
			res = MessageHandler.getMessage(ServiceMessage.VALIDATION_DATE_INTRODUCED, o);
			break;
		case ILLEGAL_DATE_2:
			res = MessageHandler.getMessage(ServiceMessage.VALIDATION_DATE_DISCONTINUED, o);
			break;
		case SECOND_DATE_AFTER_FAIL:
			res = MessageHandler.getMessage(ServiceMessage.COMPUTER_INTRODUCED_AFTER, null);
			break;
		}
		return res;
	}

	private String manageStringTypeError(ValidatorStringException e) {
		String res = "";
		switch (e.getTypeError()) {
		case BAD_LENGTH:
			res = MessageHandler.getMessage(ServiceMessage.VALIDATION_NAME_LENGTH,
					Arrays.asList(Computer.TAILLE_MIN_NAME, Computer.TAILLE_MAX_NAME).toArray());
			break;
		case ILLEGAL_CHARACTERS:
			res = MessageHandler.getMessage(ServiceMessage.SPECIAL_CHARACTERS, null);
			break;
		case NULL_STRING: res = MessageHandler.getMessage(ServiceMessage.VALIDATION_NAME_NULL, null);
			break;
		}
		return res;
	}
}
