package com.excilys.cdb.webservices;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dao.CompanyCrudDao.PageCompanyOrder;
import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.excilys.cdb.webservices.exceptions.IllegalSearchException;
import com.excilys.cdb.webservices.exceptions.WebServiceException;
import com.excilys.cdb.webservices.ressources.DefaultValues;
import com.excilys.cdb.webservices.ressources.UrlParams;

@RestController
@RequestMapping("/v1/company")
public class CompanyWebService {

	private ICompanyService serviceCompany;

	public CompanyWebService(ICompanyService companyService) {
		this.serviceCompany = companyService;
	}

	private enum Fetch{
		LAZY, EAGER;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDTO> get(@PathVariable(UrlParams.COMPANY_ID) Long id,
			@RequestParam(name = UrlParams.FETCH, required = false) String fetchS) {
		
		Fetch fetch = (fetchS == null) ? Fetch.LAZY : Fetch.valueOf(fetchS);
		CompanyDTO company = null;
		try {
			switch(fetch) {
				case EAGER: company = serviceCompany.get(id); break;
				case LAZY: company = serviceCompany.getLazy(id);break;
			}
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<CompanyDTO>(company, HttpStatus.OK);
		
	}

	@GetMapping
	public ResponseEntity<Page<CompanyDTO>> page(
			@RequestParam(name = UrlParams.PAGE, required = false) Integer iNumpage,
			@RequestParam(name = UrlParams.LIMIT, required = false) Integer paramLimit,
			@RequestParam(name = UrlParams.ORDER, required = false) String order,
			@RequestParam(name = UrlParams.FETCH, required = false) String fetchS) {
		
		Fetch fetch = (fetchS == null) ? Fetch.LAZY : Fetch.valueOf(fetchS);
		PageCompanyOrder pageOrder = (order == null) ? PageCompanyOrder.BY_NAME : PageCompanyOrder.valueOf(order);
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		int iPage = (iNumpage == null) ? DefaultValues.DEFAULT_PAGE : iNumpage;
		Page<CompanyDTO> page = null;
		try {
			switch(fetch) {
				case EAGER: page = serviceCompany.getPage(iPage, limit, pageOrder); break;
				case LAZY: page = serviceCompany.getPageLazy(iPage, limit, pageOrder); break;
			}
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Page<CompanyDTO>>(page, HttpStatus.OK);
		
	}

	@GetMapping(params = { UrlParams.SEARCH })
	public ResponseEntity<Page<CompanyDTO>> searchPage(
			@RequestParam(name = UrlParams.SEARCH, required = true) String search,
			@RequestParam(name = UrlParams.PAGE, required = false) Integer iNumpage,
			@RequestParam(name = UrlParams.LIMIT, required = false) Integer paramLimit,
			@RequestParam(name = UrlParams.ORDER, required = false) String order,
			@RequestParam(name = UrlParams.FETCH, required = false) String fetchS) {
		
		if (!SecurityTextValidation.valideString(search)) {
			throw new IllegalSearchException();
		}
		Fetch fetch = (fetchS == null) ? Fetch.LAZY : Fetch.valueOf(fetchS);
		PageCompanyOrder pageOrder = (order == null) ? PageCompanyOrder.BY_NAME : PageCompanyOrder.valueOf(order);
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		int iPage = (iNumpage == null) ? DefaultValues.DEFAULT_PAGE : iNumpage;
		Page<CompanyDTO> page = null;
		ResponseEntity<Page<CompanyDTO>> res;
		try {
			switch(fetch) {
				case EAGER: page = serviceCompany.getPageSearch(search, iPage, limit, pageOrder); break;
				case LAZY: page = serviceCompany.getPageSearchLazy(search, iPage, limit, pageOrder); break;
			}
			res = new ResponseEntity<Page<CompanyDTO>>(page, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			res = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return res;
		
	}

	/**
	 * Suppression des computers.
	 * @param deletes l'id des computers à supprimer.
	 * @return la réponse en json.
	 */
	@DeleteMapping
	public ResponseEntity<Long> delete(
			@RequestParam(name = UrlParams.DELETE, required = true) Set<Long> deletes) {
		Long elements = 0L;
		try {
			elements = serviceCompany.deleteAll(deletes);
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Long>(elements, HttpStatus.OK);
	}
	
	/**
	 * Suppression d'un computer
	 * @param id l'id du computer à supprimer
	 * @return la réponse en json.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Long> delete(@PathVariable(UrlParams.COMPANY_ID) Long id) {
		Long elements = 0L;
		try {
			elements = serviceCompany.delete(id);
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Long>(elements, HttpStatus.OK);
	}
	
	/**
	 * Ajouter d'un computer.
	 * @param computer computerdto
	 * @param bindingResult validation
	 * @return jsp de redirection.
	 */
	@PostMapping
	public ResponseEntity<Long> add(@RequestBody @Valid CompanyDTO company) {
		Company c = MapperCompany.map(company);
		Long res = 0L;
		try {
			res = serviceCompany.create(c);
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Long>(res, HttpStatus.OK);
	}

	/**
	 * Edition d'un computer.
	 * @param computer computerdto
	 * @param bindingResult validation
	 * @return jsp de redirection.
	 */
	@PutMapping
	public ResponseEntity<Boolean> edit(@RequestBody @Valid CompanyDTO company) {
		Company c = MapperCompany.map(company);
		Boolean res = false;
		try {
			res = serviceCompany.update(c);
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Boolean>(res, HttpStatus.OK);
	}

}
