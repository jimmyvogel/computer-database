package com.excilys.cdb.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servlet.ressources.JspRessources;
import com.excilys.cdb.servlet.ressources.UrlID;
import com.excilys.cdb.servlet.ressources.UrlRessources;

@RestController
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyService serviceCompany;

	public static final String SEARCH_COMPANY = "searchCompany";
	public static final String LIST_COMPANIES = "listCompanies";

	/**
	 * Direction liste des compagnies.
	 * @param numeropage le numero de la page à afficher.
	 * @param limit nombres de résultats par bloc
	 * @param model model.
	 * @return nom de la jsp
	 */
	@GetMapping("/" + LIST_COMPANIES)
	public String liste(@RequestParam(UrlID.PAGE) Integer numeropage,
			@RequestParam(UrlID.LIMIT) Integer limit, Model model) {
		Page<Company> page = new Page<Company>(limit, 0);
		try {
			page = serviceCompany.getPage(numeropage, limit);
		} catch (ServiceException e) {
			model.addAttribute(JspRessources.ERROR, e.getMessage());
		}
		model.addAttribute(UrlID.ACTION_PAGINATION, LIST_COMPANIES);
		model.addAttribute(UrlID.PAGE, page);
		return UrlRessources.LIST_COMPANIES;
	}

	/**
	 * Résultat recherche des compagnies.
	 * @param search la recherche.
	 * @param numeropage le numero de la page à afficher.
	 * @param limit nombres de résultats par bloc
	 * @param model model.
	 * @return nom de la jsp
	 */
	@GetMapping("/" + SEARCH_COMPANY)
	public String liste(@RequestParam(UrlID.SEARCH) String search,
			@RequestParam(UrlID.PAGE) Integer numeropage, @RequestParam(UrlID.LIMIT) Integer limit,
			Model model) {
		Page<Company> page = new Page<Company>(limit, 0);
		try {
			page = serviceCompany.getPageSearch(search, numeropage, limit);
		} catch (ServiceException e) {
			model.addAttribute(JspRessources.ERROR, e.getMessage());
		}
		model.addAttribute(UrlID.ACTION_PAGINATION, SEARCH_COMPANY);
		model.addAttribute(UrlID.PAGE, page);
		return UrlRessources.LIST_COMPANIES;
	}

}
