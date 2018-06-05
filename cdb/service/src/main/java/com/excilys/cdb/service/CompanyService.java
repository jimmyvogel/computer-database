package com.excilys.cdb.service;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.CDBPage;
import com.excilys.cdb.dao.CompanyCrudDao;
import com.excilys.cdb.defaultvalues.DefaultValues;
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
import com.excilys.cdb.validator.exceptions.ValidatorDateException;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Service permettant de gérer les requêtes de gestion de la table computer et
 * de la table company qui lui est lié.
 * @author vogel
 *
 */
@Service
public class CompanyService implements ICompanyService {

	//private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);

	@Autowired
	private CompanyCrudDao companyDao;
	@Autowired
	private IComputerService computerService;

	private CompanyService() {
	}

	@Override
	public List<Company> getAll() throws ServiceException {
		return companyDao.findAll();
	}

	@Override
	public CDBPage<Company> getPage(final int page) throws ServiceException {
		return getPage(page, null);
	}

	@Override
	public CDBPage<Company> getPage(final int page, final Integer limit) throws ServiceException {
		CDBPage<Company> pageCompany = null;
		int nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		Page<Company> qpage = companyDao.findAll(new QPageRequest(page - 1, nbElements));
		pageCompany = new CDBPage<Company>(nbElements, qpage.getTotalElements());
		pageCompany.charge(qpage.getContent(), page);
		return pageCompany;
	}

	@Override
	public CDBPage<Company> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		if (search == null) {
			throw new ServiceException(MessageHandler.getMessage(ServiceMessage.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new CharacterSpeciauxException();
		}
		Integer nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		CDBPage<Company> pageCompany = new CDBPage<Company>(nbElements, 0);
		if (search != null) {
			Page<Company> qpage = companyDao.findByNameContainingOrderByName(search,
					new QPageRequest(page - 1, nbElements));
			pageCompany = new CDBPage<Company>(nbElements, qpage.getTotalElements());
			pageCompany.charge(qpage.getContent(), page);
		}
		return pageCompany;
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
	public void delete(long id) throws ServiceException {
		computerService.deleteAllByCompanyId(Collections.singleton(id));
		companyDao.deleteById(id);
	}

	@Override
	public void deleteAll(Set<Long> ids) throws ServiceException {
		if (ids == null || ids.size() == 0) {
			throw new IllegalArgumentException();
		}
		computerService.deleteAllByCompanyId(ids);
		companyDao.deleteAllByIdIn(ids);
		// throw new
		// ServiceException(ExceptionHandler.getMessage(MessageException.DELETE_FAIL,
		// null));
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
		// TODO Auto-generated method stub
		return false;
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
