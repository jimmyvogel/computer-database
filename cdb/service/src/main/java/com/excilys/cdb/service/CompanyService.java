package com.excilys.cdb.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.CompanyCrudDao;
import com.excilys.cdb.dao.CompanyCrudDao.PageCompanyOrder;
import com.excilys.cdb.dao.DaoOrder;
import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.MapperCompany;
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
	public List<CompanyDTO> getAllLazy() throws ServiceException {
		return companyDao.findAll().stream().map(s -> MapperCompany.map(s)).collect(Collectors.toList());
	}
	
	@Override
	public List<CompanyDTO> getAll() throws ServiceException {
		return companyDao.findAll().stream().map(s -> MapperCompany.mapLazy(s)).collect(Collectors.toList());
	}

	@Override
	public Page<CompanyDTO> getPage(final int page) throws ServiceException {
		return getPage(page, null);
	}

	@Override
	public Page<CompanyDTO> getPage(final int page, final Integer limit) throws ServiceException {
		return getPage(page, limit, PageCompanyOrder.BY_NAME);
	}
	
	@Override
	public Page<CompanyDTO> getPageLazy(final int page) throws ServiceException {
		return getPageLazy(page, null);
	}

	@Override
	public Page<CompanyDTO> getPageLazy(final int page, final Integer limit) throws ServiceException {
		return getPageLazy(page, limit, PageCompanyOrder.BY_NAME);
	}

	@Override
	public Page<CompanyDTO> getPage(final int page, final Integer limit, DaoOrder order) throws ServiceException {
		return MapperCompany.mapToPage(page(page, limit, order));
	}
	
	@Override
	public Page<CompanyDTO> getPageLazy(final int page, final Integer limit, DaoOrder order) throws ServiceException {
		return MapperCompany.mapToPageLazy(page(page, limit, order));
	}
	
	private Page<Company> page(final int page, final Integer limit, DaoOrder order) throws ServiceException {
		if (!(order instanceof PageCompanyOrder)) {
			throw new ServiceException("Bad order enum uses");
		}

		int nbElements = (limit == null) ? DefaultValues.DEFAULT_LIMIT : limit;
		int tpage = page < 1 ? 1 : page;

		Page<Company> companies = null;
		switch ((PageCompanyOrder) order) {
		case BY_NAME:
			companies = companyDao.findAllByOrderByName(new QPageRequest(tpage - 1, nbElements));
			break;
		case BY_NAME_DESC:
			companies = companyDao.findAllByOrderByNameDesc(new QPageRequest(tpage - 1, nbElements));
			break;
		}
		return companies;
	}

	@Override
	public Page<CompanyDTO> getPageSearch(final String search, final int page, final Integer limit)
			throws ServiceException {
		return getPageSearch(search, page, limit, PageCompanyOrder.BY_NAME);
	}
	
	@Override
	public Page<CompanyDTO> getPageSearchLazy(final String search, final int page, final Integer limit)
			throws ServiceException {
		return getPageSearchLazy(search, page, limit, PageCompanyOrder.BY_NAME);
	}
	
	@Override
	public Page<CompanyDTO> getPageSearch(final String search, final int page, final Integer limit, final PageCompanyOrder order)
			throws ServiceException {
		return MapperCompany.mapToPage(pageSearch(search, page, limit, order));
	}
	
	@Override
	public Page<CompanyDTO> getPageSearchLazy(final String search, final int page, final Integer limit, final PageCompanyOrder order)
			throws ServiceException {
		return MapperCompany.mapToPageLazy(pageSearch(search, page, limit, order));
	}
	
	private Page<Company> pageSearch(final String search, final int page, final Integer limit, final PageCompanyOrder order) throws ServiceException{
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
			switch (order) {
				case BY_NAME: qpage = companyDao.findByNameContainingOrderByName(search, new QPageRequest(tpage - 1, nbElements)); break;
				case BY_NAME_DESC: qpage = companyDao.findByNameContainingOrderByNameDesc(search, new QPageRequest(tpage - 1, nbElements)); break;
			}
		}
		return qpage;
	}

	@Override
	public CompanyDTO get(long id) throws ServiceException {
		Company company = companyDao.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
		return MapperCompany.map(company);
	}
	
	@Override
	public CompanyDTO getLazy(long id) throws ServiceException {
		Company company = companyDao.findById(id).orElseThrow(() -> new CompanyNotFoundException(id));
		return MapperCompany.mapLazy(company);
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
	public long create(CompanyDTO t) throws ServiceException {
		Company create = null;
		try {
			create = MapperCompany.map(t);
		} catch (ValidatorStringException e) {
			throw new ServiceException(manageStringTypeError(e));
		}
		return companyDao.save(create).getId() ;
	}

	@Override
	public boolean update(CompanyDTO  update) throws ServiceException {
		Company up = null;
		try {
			up = MapperCompany.map(update);
		} catch (ValidatorStringException e) {
			throw new ServiceException(manageStringTypeError(e));
		}
		return companyDao.save(up) != null;
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
		case NULL_STRING:
			res = MessageHandler.getMessage(ServiceMessage.VALIDATION_NAME_NULL, null);
			break;
		}
		return res;
	}
}
