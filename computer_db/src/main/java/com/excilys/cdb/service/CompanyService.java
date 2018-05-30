package com.excilys.cdb.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CDBPage;
import com.excilys.cdb.persistence.CompanyCrudDao;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.exceptions.CompanyNotFoundException;
import com.excilys.cdb.service.exceptions.NameInvalidException;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servlet.ressources.DefaultValues;
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
@Transactional
public class CompanyService implements ICompanyService {

	@Autowired
	private CompanyCrudDao companyDao;

	@Override
	@Transactional(readOnly = true)
	public List<Company> getAll() throws ServiceException {
		return companyDao.findAll();
	}

	@Override
	public CDBPage<Company> getPage(final int page) throws ServiceException {
		return getPage(page, null);
	}

	@Override
	@Transactional(readOnly = true)
	public CDBPage<Company> getPage(final int page, final Integer limit) throws ServiceException {
		CDBPage<Company> pageCompany = null;
		Integer nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		Page<Company> qpage = companyDao.findAll(new QPageRequest(page - 1, nbElements));
		pageCompany = new CDBPage<Company>(limit, qpage.getTotalElements());
		pageCompany.charge(qpage.getContent(), page);
		return pageCompany;
	}

	@Override
	@Transactional(readOnly = true)
	public CDBPage<Company> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		if (search == null) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(search)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		Integer nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		CDBPage<Company> pageCompany = new CDBPage<Company>(nbElements, 0);
		if (search != null) {
			Page<Company> qpage = companyDao.findByNameContainingOrderByName(search, new QPageRequest(page - 1, nbElements));
			pageCompany = new CDBPage<Company>(nbElements, qpage.getTotalElements());
			pageCompany.charge(qpage.getContent(), page);
		}
		return pageCompany;
	}

	@Override
	@Transactional(readOnly = true)
	public Company get(long id) throws ServiceException {
		Company company = companyDao.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
		return company;
	}

	@Override
	public long create(final String name) throws ServiceException {
		long result = -1;
		if (name == null || name.isEmpty()) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, null));
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.SPECIAL_CHARACTERS, null));
		}
		try {
			CompanyValidator.validName(name);
			Company c = new Company(name);
			result = companyDao.save(c).getId();
		} catch (ValidatorStringException e) {
			throw new NameInvalidException(e.getMessage());
		}
		return result;
	}

	@Override
	public void delete(long id) throws DaoException {
		companyDao.deleteById(id);
	}

	@Override
	public void deleteAll(Set<Long> ids) throws ServiceException {
		if (ids == null || ids.size() == 0) {
			throw new ServiceException(ExceptionHandler.getMessage(MessageException.ILLEGAL_ARGUMENTS, null));
		}
		companyDao.deleteAllById(ids);
		// throw new
		// ServiceException(ExceptionHandler.getMessage(MessageException.DELETE_FAIL,
		// null));
	}

	@Override
	@Transactional(readOnly = true)
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
}
