package com.excilys.cdb.servlet;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servlet.ressources.DefaultValues;
import com.excilys.cdb.servlet.ressources.JspRessources;
import com.excilys.cdb.servlet.ressources.UrlID;
import com.excilys.cdb.servlet.ressources.UrlRessources;
import com.excilys.cdb.validator.ComputerValidator;

@RestController
@RequestMapping("/computer")
public class ComputerController {

	@Autowired
	private ComputerService serviceComputer;

	public static final String ADD_COMPUTER = "addComputer";
	public static final String EDIT_COMPUTER = "editComputer";
	public static final String SEARCH_COMPUTER = "searchComputer";
	public static final String DELETE_COMPUTER = "deleteComputer";
	public static final String ADD_FORM_COMPUTER = "addFormComputer";
	public static final String EDIT_FORM_COMPUTER = "editFormComputer";
	public static final String LIST_COMPUTERS = "listComputers";

	/**
	 * Direction liste des compagnies.
	 * @param numeropage le numero de la page à afficher.
	 * @param limit nombres de résultats par bloc
	 * @param model model.
	 * @return nom de la jsp
	 */
	@GetMapping("/" + LIST_COMPUTERS)
	public String liste(@RequestParam(UrlID.PAGE) Integer numeropage,
			@RequestParam(UrlID.LIMIT) Integer limit, Model model) {
		Page<Computer> page = new Page<Computer>(limit, 0);
		try {
			page = serviceComputer.getPage(numeropage, limit);
		} catch (ServiceException e) {
			model.addAttribute(JspRessources.ERROR, e.getMessage());
		}
		model.addAttribute(UrlID.ACTION_PAGINATION, LIST_COMPUTERS);
		model.addAttribute(UrlID.PAGE, page);
		return UrlRessources.LIST_COMPUTERS;
	}

	/**
	 * Résultat recherche des compagnies.
	 * @param search la recherche.
	 * @param numeropage le numero de la page à afficher.
	 * @param limit nombres de résultats par bloc
	 * @param model model.
	 * @return nom de la jsp
	 */
	@GetMapping("/" + SEARCH_COMPUTER)
	public String liste(@RequestParam(UrlID.SEARCH) String search,
			@RequestParam(UrlID.PAGE) Integer numeropage, @RequestParam(UrlID.LIMIT) Integer limit,
			Model model) {
		Page<Computer> page = new Page<Computer>(limit, 0);
		try {
			page = serviceComputer.getPageSearch(search, numeropage, limit);
		} catch (ServiceException e) {
			model.addAttribute(JspRessources.ERROR, e.getMessage());
		}
		model.addAttribute(UrlID.ACTION_PAGINATION, SEARCH_COMPUTER);
		model.addAttribute(UrlID.PAGE, page);
		return UrlRessources.LIST_COMPUTERS;
	}

	/**
	 * Suppression des computers.
	 * @param deletes l'id des computers à supprimer dans un string.
	 * @param model model.
	 * @return nom de la jsp.
	 */
	@PostMapping("/" + DELETE_COMPUTER)
	public String delete(@RequestParam(JspRessources.DELETE_SELECT) String deletes, Model model) {
		String[] selections = deletes.split(",");
		Set<Long> set = Arrays.stream(selections).map(l -> Long.valueOf(l)).collect(Collectors.toSet());
		try {
			serviceComputer.deleteAll(set);
			model.addAttribute(JspRessources.SUCCESS, "Delete computer " + Arrays.toString(selections) + " success.");
		} catch (DaoException e) {
			model.addAttribute(JspRessources.ERROR, e.getMessage());
		}
		return liste(DefaultValues.DEFAULT_PAGE, DefaultValues.DEFAULT_LIMIT, model);
	}

	/**
	 * Ajouter d'un computer.
	 * @param nameS le nom du computer
	 * @param introducedS la date d'introduction
	 * @param discontinuedS la date de termination
	 * @param idCompanyS l'id de la company du computer
	 * @param model model
	 * @return nom de la jsp de redirection.
	 */
	@PostMapping("/" + ADD_COMPUTER)
	public String add(@RequestParam(JspRessources.FORM_COMPUTER_PARAM_NAME) String nameS,
			@RequestParam(JspRessources.FORM_COMPUTER_PARAM_INTRODUCED) String introducedS,
			@RequestParam(JspRessources.FORM_COMPUTER_PARAM_DISCONTINUED) String discontinuedS,
			@RequestParam(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY) String idCompanyS,
			Model model) {

		Optional<Computer> c = ComputerValidator.validComputer(nameS, introducedS, discontinuedS, idCompanyS);
		if (c.isPresent()) {
			try {
				serviceComputer.create(c.get());
			} catch (ServiceException | DaoException e) {
				model.addAttribute(JspRessources.ERROR, e.getMessage());
			}
			model.addAttribute(JspRessources.SUCCESS, "Update Computer success.");
		} else {
			model.addAttribute(JspRessources.ERROR, "Invalide arguments");
		}
		return formAdd(model);
	}

	/**
	 * Redirection vers le form d'addtion d'un computer.
	 * @param model le model.
	 * @return nom de la jsp de redirection.
	 */
	private String formAdd(Model model) {
		return null;
	}

	/**
	 * Edition d'un computer.
	 * @param nameS le nom du computer
	 * @param introducedS la date d'introduction
	 * @param discontinuedS la date de termination
	 * @param idCompanyS l'id de la company du computer
	 * @param model model
	 * @return nom de la jsp de redirection.
	 */
	@PostMapping("/" + EDIT_COMPUTER)
	public String edit(@RequestParam(JspRessources.FORM_COMPUTER_PARAM_NAME) String nameS,
			@RequestParam(JspRessources.FORM_COMPUTER_PARAM_INTRODUCED) String introducedS,
			@RequestParam(JspRessources.FORM_COMPUTER_PARAM_DISCONTINUED) String discontinuedS,
			@RequestParam(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY) String idCompanyS,
			Model model) {
		Optional<Computer> c = ComputerValidator.validComputer(nameS, introducedS, discontinuedS, idCompanyS);
		if (c.isPresent()) {
			try {
				serviceComputer.update(c.get());
			} catch (ServiceException | DaoException e) {
				model.addAttribute(JspRessources.ERROR, e.getMessage());
			}
			model.addAttribute(JspRessources.SUCCESS, "Update Computer success.");
		} else {
			model.addAttribute(JspRessources.ERROR, "Invalide arguments");
		}
		return formEdit(model);
	}

	/**
	 * Redirection vers le form d'édition d'un computer
	 * @param model le model.
	 * @return nom de la jsp de redirection.
	 */
	private String formEdit(Model model) {
		return UrlRessources.FORM_EDIT_COMPUTER;
	}

}
