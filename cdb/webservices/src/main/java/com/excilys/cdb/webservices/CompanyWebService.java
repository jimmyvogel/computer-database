package com.excilys.cdb.webservices;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/company")
public class CompanyWebService {

	private ICompanyService serviceCompany;

	public CompanyWebService(ICompanyService companyService) {
		this.serviceCompany = companyService;
	}

	public static final String SEARCH_COMPANY = "searchCompany";
	public static final String LIST_COMPANIES = "listCompanies";

	@GetMapping(params= {UrlParams.COMPANY_ID})
	public ResponseEntity<CompanyDTO> get(@RequestParam(name = UrlParams.COMPANY_ID) Long id){
		Optional<CompanyDTO> company = null;
		try {
			company = MapperCompany.map(serviceCompany.get(id));
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		ResponseEntity<CompanyDTO> e;
		if(company.isPresent()) {
			e = new ResponseEntity<CompanyDTO>(company.get(), HttpStatus.OK);
		} else {
			e = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return e;
	}
	
	@GetMapping
	public ResponseEntity<Page<CompanyDTO>> page(@RequestParam(name = UrlParams.PAGE, required=true) Integer iNumpage,
			@RequestParam(UrlParams.LIMIT) Integer paramLimit) {
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		Page<Company> page = null;
		Page<CompanyDTO> pageImpl = null;
		try {
			page = serviceCompany.getPage(iNumpage, limit);
			List<CompanyDTO> list = page.getContent().stream().map(c -> MapperCompany.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<CompanyDTO>(list, page.getPageable(), page.getTotalElements());
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Page<CompanyDTO>>(pageImpl, HttpStatus.OK);
	}

	@GetMapping(params= {UrlParams.SEARCH})
	public ResponseEntity<Page<CompanyDTO>> searchPage(@RequestParam(name=UrlParams.SEARCH, required=true) String search,
			@RequestParam(name=UrlParams.PAGE, required=true) Integer iNumpage, @RequestParam(UrlParams.LIMIT) Integer paramLimit) {
		if (!SecurityTextValidation.valideString(search)) {
			throw new IllegalSearchException();
		}
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		Page<Company> page = null;
		Page<CompanyDTO> pageImpl = null;
		ResponseEntity<Page<CompanyDTO>> res;
		try {
			page = serviceCompany.getPageSearch(search, iNumpage, limit);
			List<CompanyDTO> list = page.getContent().stream().map(c -> MapperCompany.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<CompanyDTO>(list, page.getPageable(), page.getTotalElements());
			res = new ResponseEntity<Page<CompanyDTO>>(pageImpl, HttpStatus.OK);
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
	public ResponseEntity<Long> delete(@RequestBody(required = true) HashSet<Long> deletes) {
		Long elements = 0L;
		try {
			elements = serviceCompany.deleteAll(deletes);
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
		Optional<Company> c = MapperCompany.map(company);
		Long res = 0L;
		if (c.isPresent()) {
			try {
				res = serviceCompany.create(c.get());
			} catch (ServiceException e) {
				throw new WebServiceException(e);
			}
		} else {
			return new ResponseEntity<Long>(res, HttpStatus.NOT_FOUND);
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
		Optional<Company> c = MapperCompany.map(company);
		Boolean res = false;
		if (c.isPresent()) {
			try {
				res = serviceCompany.update(c.get());
			} catch (ServiceException e) {
				throw new WebServiceException(e);
			}
		} else {
			return new ResponseEntity<Boolean>(res, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Boolean>(res, HttpStatus.OK);
	}

}
