package com.excilys.cdb.webservices.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.CompanyDTO;
import com.excilys.cdb.mapper.MapperCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.SecurityTextValidation;
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

	@GetMapping
	public ResponseEntity<Page<CompanyDTO>> pageCompanies(@RequestParam(name = UrlParams.PAGE, required=true) Integer iNumpage,
			@RequestParam(UrlParams.LIMIT) Integer paramLimit) {
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		Page<Company> page = null;
		Page<CompanyDTO> pageImpl = null;
		try {
			page = serviceCompany.getPage(iNumpage, limit);
			List<CompanyDTO> list = page.getContent().stream().map(c -> MapperCompany.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<CompanyDTO>(list, page.getPageable(), page.getTotalElements());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<CompanyDTO>>(pageImpl, HttpStatus.OK);
	}

	@GetMapping(params= {UrlParams.SEARCH})
	public ResponseEntity<Page<CompanyDTO>> searchCompanies(@RequestParam(name=UrlParams.SEARCH, required=true) String search,
			@RequestParam(name=UrlParams.PAGE, required=true) Integer iNumpage, @RequestParam(UrlParams.LIMIT) Integer paramLimit) {
		if (!SecurityTextValidation.valideString(search)) {
			//TODO ERROR
		}
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		Page<Company> page = null;
		Page<CompanyDTO> pageImpl = null;
		try {
			page = serviceCompany.getPageSearch(search, iNumpage, limit);
			List<CompanyDTO> list = page.getContent().stream().map(c -> MapperCompany.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<CompanyDTO>(list, page.getPageable(), page.getTotalElements());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<CompanyDTO>>(pageImpl, HttpStatus.OK);
	}

}
