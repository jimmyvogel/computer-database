package com.excilys.cdb.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.CompanyCrudDao;
import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.exceptions.CharacterSpeciauxException;
import com.excilys.cdb.service.exceptions.CompanyNotFoundException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servicemessage.ServiceMessage;
import com.excilys.cdb.validator.CompanyValidator;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * @author vogel
 *
 */
@Service
public class CompanyService implements ICompanyService {

	private CompanyCrudDao companyDao;
	private IComputerService computerService;

	public CompanyService(CompanyCrudDao companyDao, IComputerService computerService) {
		this.companyDao = companyDao;
		this.computerService = computerService;
	}

	@Override
	public List<Company> getAll() throws ServiceException {
		return companyDao.findAll();
	}

	@Override
	public Page<Company> getPage(final int page) throws ServiceException {
		return getPage(page, null);
	}

	@Override
	public Page<Company> getPage(final int page, final Integer limit) throws ServiceException {
		int nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		int tpage = page < 1 ? 1 : page;
		return companyDao.findAll(new QPageRequest(tpage - 1, nbElements));
	}

	@Override
	public Page<Company> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		if (search == null) {
			throw new ServiceException(MessageHandler.getMessage(ServiceMessage.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new CharacterSpeciauxException();
		}
		Integer nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		int tpage = page < 1 ? 1 : page;
		Page<Company> qpage = null;
		if (search != null) {
			qpage = companyDao.findByNameContainingOrderByName(search, new QPageRequest(tpage - 1, nbElements));
		}
		return qpage;
	}

	@Override
	public Company get(long id) throws ServiceException {
		Company company = companyDao.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
		return company;
	}

	@Override
	public long create(final String name) throws ServiceException {
		long result = -1;
		try {
			CompanyValidator.validName(name);
			Company c = new Company(name);
			result = companyDao.save(c).getId();
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(manageStringTypeError(e));
		}
		return result;
	}

	@Override
	public Long delete(long id) throws ServiceException {
		Long elems = computerService.deleteAllByCompanyId(Collections.singleton(id));
		companyDao.deleteById(id);
		return elems + 1;
	}

	@Override
	public Long deleteAll(Set<Long> ids) throws ServiceException {
		if (ids == null || ids.size() == 0) {
			throw new IllegalArgumentException();
		}
		Long elems = computerService.deleteAllByCompanyId(ids);
		return companyDao.deleteAllByIdIn(ids) + elems;
	}

	@Override
	public long count() throws ServiceException {
		return companyDao.count();
	}

	@Override
	public long create(Company t) throws ServiceException {
		return create(t.getName());
	}

	@Override
	public boolean update(Company update) throws ServiceException {
		return companyDao.save(update) != null;
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
