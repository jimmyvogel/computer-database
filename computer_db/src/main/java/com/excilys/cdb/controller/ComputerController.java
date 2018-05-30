package com.excilys.cdb.controller;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.mapper.PageMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.CDBPage;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.servlet.ressources.DefaultValues;
import com.excilys.cdb.servlet.ressources.JspRessources;
import com.excilys.cdb.servlet.ressources.UrlID;
import com.excilys.cdb.servlet.ressources.UrlRessources;

@Controller
@RequestMapping("/computer")
public class ComputerController {

	@Autowired
	private IComputerService serviceComputer;
	@Autowired
	private ICompanyService serviceCompany;

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);

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
	 * @return nom de la jsp
	 */
	@GetMapping("/" + LIST_COMPUTERS)
	public ModelAndView liste(@RequestParam(UrlID.PAGE) Integer numeropage, @RequestParam(UrlID.LIMIT) Integer limit) {
		CDBPage<ComputerDTO> page = new CDBPage<ComputerDTO>(limit, 0);
		ModelAndView mv = new ModelAndView(UrlRessources.LIST_COMPUTERS);
		try {
			page = PageMapper.mapPageComputerToDto(serviceComputer.getPage(numeropage, limit));
		} catch (ServiceException e) {
			mv.addObject(JspRessources.ERROR, e.getMessage());
		}
		mv.addObject(UrlID.ACTION_PAGINATION, LIST_COMPUTERS);
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
	@GetMapping("/" + SEARCH_COMPUTER)
	public ModelAndView search(@RequestParam(UrlID.SEARCH) String search, @RequestParam(value = UrlID.PAGE, required = false) Integer iNumpage,
			@RequestParam(value = UrlID.LIMIT, required = false) Integer paramLimit) {
		int numpage = (iNumpage == null) ? 1 : iNumpage;
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		CDBPage<Computer> page = new CDBPage<Computer>(limit, 0);
		ModelAndView mv = new ModelAndView(UrlRessources.LIST_COMPUTERS);
		try {
			page = serviceComputer.getPageSearch(search, numpage, limit);
		} catch (ServiceException e) {
			mv.addObject(JspRessources.ERROR, e.getMessage());
		}
		mv.addObject(UrlID.SEARCH, search);
		//mv.addObject(UrlID.ACTION_PAGINATION, SEARCH_COMPUTER);
		mv.addObject(UrlID.PAGE, page);
		return mv;
	}

	/**
	 * Suppression des computers.
	 * @param deletes l'id des computers à supprimer dans un string.
	 * @return nom de la jsp.
	 */
	@PostMapping("/" + DELETE_COMPUTER)
	public ModelAndView delete(@RequestParam(JspRessources.DELETE_SELECT) String deletes) {
		String[] selections = deletes.split(",");
		Set<Long> set = Arrays.stream(selections).map(l -> Long.valueOf(l)).collect(Collectors.toSet());
		ModelAndView mv;
		try {
			serviceComputer.deleteAll(set);
			mv = liste(DefaultValues.DEFAULT_PAGE, DefaultValues.DEFAULT_LIMIT);
			mv.addObject(JspRessources.SUCCESS, "Delete computer " + Arrays.toString(selections) + " success.");
		} catch (ServiceException e) {
			mv = liste(DefaultValues.DEFAULT_PAGE, DefaultValues.DEFAULT_LIMIT);
			mv.addObject(JspRessources.ERROR, e.getMessage());
		}
		return mv;
	}

	/**
	 * Ajouter d'un computer.
	 * @param computer computerdto
	 * @param bindingResult validation
	 * @return jsp de redirection.
	 */
	@PostMapping("/" + ADD_COMPUTER)
	public ModelAndView add(@ModelAttribute @Valid ComputerDTO computer, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView mv = formAdd();
			for (ObjectError oe : bindingResult.getAllErrors()) {
				mv.addObject(JspRessources.ERROR, oe.getDefaultMessage());
			}
			return mv;
		}
		Optional<Computer> c = MapperComputer.map(computer);
		ModelAndView mv = formAdd();
		if (c.isPresent()) {
			try {
				serviceComputer.create(c.get());
				mv.addObject(JspRessources.SUCCESS, "Create Computer success.");
			} catch (ServiceException e) {
				mv.addObject(JspRessources.ERROR, e.getMessage());
			}
		} else {
			mv.addObject(JspRessources.ERROR, "Invalide arguments");
		}
		return mv;
	}

	/**
	 * Redirection vers le form d'addtion d'un computer.
	 * @return jsp de redirection.
	 */
	@GetMapping("/" + ADD_FORM_COMPUTER)
	private ModelAndView formAdd() {
		ModelAndView mv = new ModelAndView(UrlRessources.FORM_ADD_COMPUTER);
		try {
			mv.addObject(JspRessources.ALL_COMPANY, serviceCompany.getAll());
		} catch (ServiceException e) {
			mv.addObject(JspRessources.ERROR, e.getMessage());
		}
		return mv;
	}

	/**
	 * Edition d'un computer.
	 * @param computer computerdto
	 * @param bindingResult validation
	 * @return jsp de redirection.
	 */
	@PostMapping("/" + EDIT_COMPUTER)
	public ModelAndView edit(@ModelAttribute @Valid ComputerDTO computer, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			ModelAndView mv = formEdit(computer.getId());
			for (ObjectError oe : bindingResult.getAllErrors()) {
				mv.addObject(JspRessources.ERROR, oe.getDefaultMessage());
			}
			return mv;
		}
		ModelAndView mv = formEdit(computer.getId());
		Optional<Computer> c = MapperComputer.map(computer);
		if (c.isPresent()) {
			try {
				System.out.println(c);
				serviceComputer.update(c.get());
				mv.addObject(JspRessources.SUCCESS, "Update Computer success.");
			} catch (ServiceException e) {
				mv.addObject(JspRessources.ERROR, e.getMessage());
			}
		} else {
			mv.addObject(JspRessources.ERROR, "Invalide arguments");
		}
		return mv;
	}

	/**
	 * Redirection vers le form d'édition d'un computer.
	 * @param id l'id du computer à modifié.
	 * @return jsp de redirection.
	 */
	@GetMapping("/" + EDIT_FORM_COMPUTER)
	public ModelAndView formEdit(@RequestParam(JspRessources.COMPUTER_ID) long id) {
		ModelAndView mv = new ModelAndView(UrlRessources.FORM_EDIT_COMPUTER);
		try {
			mv.addObject(JspRessources.COMPUTER, new ComputerDTO(serviceComputer.get(id)));
			mv.addObject(JspRessources.ALL_COMPANY, serviceCompany.getAll());
		} catch (ServiceException e) {
			mv.addObject(JspRessources.ERROR, e.getMessage());
		}
		return mv;
	}

}
