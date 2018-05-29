package com.excilys.cdb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servlet.ressources.JspRessources;
import com.excilys.cdb.servlet.ressources.UrlID;
import com.excilys.cdb.servlet.ressources.UrlRessources;

@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyService serviceCompany;

	public static final String SEARCH_COMPANY = "searchCompany";
	public static final String LIST_COMPANIES = "listCompanies";

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyController.class);

	/**
	 * Direction liste des compagnies.
	 * @param numeropage le numero de la page à afficher.
	 * @param limit nombres de résultats par bloc
	 * @return nom de la jsp
	 */
	@GetMapping("/" + LIST_COMPANIES)
	public ModelAndView liste(@RequestParam(UrlID.PAGE) Integer numeropage,
			@RequestParam(UrlID.LIMIT) Integer limit) {
		LOGGER.info("Méthode liste");
		Page<Company> page = new Page<Company>(limit, 0);
		ModelAndView mv = new ModelAndView(UrlRessources.LIST_COMPANIES);
		try {
			page = serviceCompany.getPage(numeropage, limit);
		} catch (ServiceException e) {
			mv.addObject(JspRessources.ERROR, e.getMessage());
		}
		mv.addObject(UrlID.ACTION_PAGINATION, LIST_COMPANIES);
		mv.addObject(UrlID.PAGE, page);
		return mv;
	}

	/**
	 * Résultat recherche des compagnies.
	 * @param search la recherche.
	 * @param numeropage le numero de la page à afficher.
	 * @param limit nombres de résultats par bloc
	 * @return nom de la jsp
	 */
	@GetMapping("/" + SEARCH_COMPANY)
	public ModelAndView search(@RequestParam(UrlID.SEARCH) String search,
			@RequestParam(UrlID.PAGE) Integer numeropage, @RequestParam(UrlID.LIMIT) Integer limit) {
		Page<Company> page = new Page<Company>(limit, 0);
		ModelAndView mv = new ModelAndView(UrlRessources.LIST_COMPANIES);
		try {
			page = serviceCompany.getPageSearch(search, numeropage, limit);
		} catch (ServiceException e) {
			mv.addObject(JspRessources.ERROR, e.getMessage());
		}
		mv.addObject(UrlID.ACTION_PAGINATION, SEARCH_COMPANY);
		mv.addObject(UrlID.PAGE, page);
		return mv;
	}

}
