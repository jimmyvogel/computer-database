package com.excilys.cdb.webservices.controller;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.excilys.cdb.webservices.ressources.DefaultValues;
import com.excilys.cdb.webservices.ressources.UrlParams;

@Controller
@RequestMapping("/computer")
public class ComputerWebService {

	private IComputerService serviceComputer;
	private ICompanyService serviceCompany;

	public ComputerWebService(ICompanyService companyService, IComputerService computerService) {
		this.serviceCompany = companyService;
		this.serviceComputer = computerService;
	}
	
	public static final String ADD_COMPUTER = "addComputer";
	public static final String EDIT_COMPUTER = "editComputer";
	public static final String SEARCH_COMPUTER = "searchComputer";
	public static final String DELETE_COMPUTER = "deleteComputer";
	public static final String ADD_FORM_COMPUTER = "addFormComputer";
	public static final String EDIT_FORM_COMPUTER = "editFormComputer";
	public static final String LIST_COMPUTERS = "listComputers";


	/**
	 * Liste des compagnies.
	 * @param numeropage le numero de la page à afficher (required).
	 * @param limit nombres de résultats par bloc
	 * @return la réponse en json.
	 */
	@GetMapping
	public ResponseEntity<Page<ComputerDTO>> pageComputers(@RequestParam(name = UrlParams.PAGE, required=true) Integer iNumpage,
			@RequestParam(UrlParams.LIMIT) Integer paramLimit) {
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		Page<Computer> page = null;
		Page<ComputerDTO> pageImpl = null;
		try {
			page = serviceComputer.getPage(iNumpage, limit);
			List<ComputerDTO> list = page.getContent().stream().map(c -> MapperComputer.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<ComputerDTO>(list, page.getPageable(), page.getTotalElements());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<ComputerDTO>>(pageImpl, HttpStatus.OK);
	}
//
	/**
	 * Résultat recherche des compagnies par nom.
	 * @param search la recherche (required).
	 * @param numeropage le numero de la page à afficher (required).
	 * @param limit nombres de résultats par bloc
	 * @return la réponse en json.
	 */
	@GetMapping(params= {UrlParams.SEARCH})
	public ResponseEntity<Page<ComputerDTO>> searchPageComputers(@RequestParam(name=UrlParams.SEARCH, required=true) String search,
			@RequestParam(name=UrlParams.PAGE, required=true) Integer iNumpage, @RequestParam(UrlParams.LIMIT) Integer paramLimit) {
		if (!SecurityTextValidation.valideString(search)) {
			//TODO ERROR
		}
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		Page<Computer> page = null;
		Page<ComputerDTO> pageImpl = null;
		try {
			page = serviceComputer.getPageSearch(search, iNumpage, limit);
			List<ComputerDTO> list = page.getContent().stream().map(c -> MapperComputer.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<ComputerDTO>(list, page.getPageable(), page.getTotalElements());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Page<ComputerDTO>>(pageImpl, HttpStatus.OK);
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
			elements = serviceComputer.deleteAll(deletes);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Long>(elements, HttpStatus.OK);
	}

//	/**
//	 * Ajouter d'un computer.
//	 * @param computer computerdto
//	 * @param bindingResult validation
//	 * @return jsp de redirection.
//	 */
//	@PostMapping("/" + ADD_COMPUTER)
//	public ModelAndView add(@ModelAttribute @Valid ComputerDTO computer, BindingResult bindingResult) {
//		if (bindingResult.hasErrors()) {
//			ModelAndView mv = formAdd();
//			for (ObjectError oe : bindingResult.getAllErrors()) {
//				mv.addObject(JspRessources.ERROR, oe.getDefaultMessage());
//			}
//			return mv;
//		}
//		Optional<Computer> c = MapperComputer.map(computer);
//		ModelAndView mv = formAdd();
//		if (c.isPresent()) {
//			try {
//				serviceComputer.create(c.get());
//				mv.addObject(JspRessources.SUCCESS, MessageHandler.getMessage(ControllerMessage.SUCCESS_CREATE, null));
//			} catch (ServiceException e) {
//				mv.addObject(JspRessources.ERROR, e.getMessage());
//			}
//		} else {
//			mv.addObject(JspRessources.ERROR, MessageHandler.getMessage(ControllerMessage.ILLEGAL_ARGUMENTS, null));
//		}
//		return mv;
//	}
//
//	/**
//	 * Redirection vers le form d'addtion d'un computer.
//	 * @return jsp de redirection.
//	 */
//	@GetMapping("/" + ADD_FORM_COMPUTER)
//	private ModelAndView formAdd() {
//		ModelAndView mv = new ModelAndView(UrlRessources.FORM_ADD_COMPUTER);
//		try {
//			mv.addObject(JspRessources.ALL_COMPANY, serviceCompany.getAll());
//		} catch (ServiceException e) {
//			mv.addObject(JspRessources.ERROR, e.getMessage());
//		}
//		return mv;
//	}
//
//	/**
//	 * Edition d'un computer.
//	 * @param computer computerdto
//	 * @param bindingResult validation
//	 * @return jsp de redirection.
//	 */
//	@PostMapping("/" + EDIT_COMPUTER)
//	public ModelAndView edit(@ModelAttribute @Valid ComputerDTO computer, BindingResult bindingResult) {
//		if (bindingResult.hasErrors()) {
//			ModelAndView mv = formEdit(computer.getId());
//			for (ObjectError oe : bindingResult.getAllErrors()) {
//				mv.addObject(JspRessources.ERROR, oe.getDefaultMessage());
//			}
//			return mv;
//		}
//		ModelAndView mv = formEdit(computer.getId());
//		Optional<Computer> c = MapperComputer.map(computer);
//		if (c.isPresent()) {
//			try {
//				System.out.println(c);
//				serviceComputer.update(c.get());
//				mv.addObject(JspRessources.SUCCESS, MessageHandler.getMessage(ControllerMessage.SUCCESS_UPDATE, null));
//			} catch (ServiceException e) {
//				mv.addObject(JspRessources.ERROR, e.getMessage());
//			}
//		} else {
//			mv.addObject(JspRessources.ERROR, MessageHandler.getMessage(ControllerMessage.ILLEGAL_ARGUMENTS, null));
//		}
//		return mv;
//	}
//
//	/**
//	 * Redirection vers le form d'édition d'un computer.
//	 * @param id l'id du computer à modifié.
//	 * @return jsp de redirection.
//	 */
//	@GetMapping("/" + EDIT_FORM_COMPUTER)
//	public ModelAndView formEdit(@RequestParam(JspRessources.COMPUTER_ID) long id) {
//		ModelAndView mv = new ModelAndView(UrlRessources.FORM_EDIT_COMPUTER);
//		try {
//			mv.addObject(JspRessources.COMPUTER, new ComputerDTO(serviceComputer.get(id)));
//			mv.addObject(JspRessources.ALL_COMPANY, serviceCompany.getAll());
//		} catch (ServiceException e) {
//			mv.addObject(JspRessources.ERROR, e.getMessage());
//		}
//		return mv;
//	}

}
