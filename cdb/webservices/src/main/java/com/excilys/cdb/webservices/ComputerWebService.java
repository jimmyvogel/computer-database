package com.excilys.cdb.webservices;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.cdb.dao.ComputerCrudDao.PageComputerOrder;
import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.MapperComputer;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ICompanyService;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.SecurityTextValidation;
import com.excilys.cdb.webservices.exceptions.IllegalSearchException;
import com.excilys.cdb.webservices.exceptions.WebServiceException;
import com.excilys.cdb.webservices.ressources.DefaultValues;
import com.excilys.cdb.webservices.ressources.UrlParams;

@Controller
@RequestMapping("/v1/computer")
@CrossOrigin
public class ComputerWebService {

	private IComputerService serviceComputer;

	public ComputerWebService(ICompanyService companyService, IComputerService computerService) {
		this.serviceComputer = computerService;
	}
	
	public static final String ADD_COMPUTER = "addComputer";
	public static final String EDIT_COMPUTER = "editComputer";
	public static final String SEARCH_COMPUTER = "searchComputer";
	public static final String DELETE_COMPUTER = "deleteComputer";
	public static final String ADD_FORM_COMPUTER = "addFormComputer";
	public static final String EDIT_FORM_COMPUTER = "editFormComputer";
	public static final String LIST_COMPUTERS = "listComputers";


	@GetMapping("/{id}")
	public ResponseEntity<ComputerDTO> get(@PathVariable(UrlParams.COMPUTER_ID) Long id){
		Optional<ComputerDTO> computer = null;
		try {
			computer = MapperComputer.map(serviceComputer.get(id));
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		ResponseEntity<ComputerDTO> e;
		if(computer.isPresent()) {
			e = new ResponseEntity<ComputerDTO>(computer.get(), HttpStatus.OK);
		} else {
			e = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return e;
	}
	
	/**
	 * Liste des compagnies.
	 * @param numeropage le numero de la page à afficher (required).
	 * @param limit nombres de résultats par bloc
	 * @return la réponse en json.
	 */
	@GetMapping
	public ResponseEntity<Page<ComputerDTO>> page(@RequestParam(name = UrlParams.PAGE, required=false) Integer iNumpage,
			@RequestParam(name = UrlParams.LIMIT, required=false) Integer paramLimit,
			@RequestParam(name = UrlParams.ORDER, required=false) String order) {
		PageComputerOrder pageOrder = (order == null) ? PageComputerOrder.BY_NAME  : PageComputerOrder.valueOf(order);
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		int iPage = (iNumpage == null) ? DefaultValues.DEFAULT_PAGE : iNumpage;
		Page<Computer> page = null;
		Page<ComputerDTO> pageImpl = null;
		try {
			page = serviceComputer.getPage(iPage, limit, pageOrder);
			List<ComputerDTO> list = page.getContent().stream().map(c -> MapperComputer.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<ComputerDTO>(list, page.getPageable(), page.getTotalElements());
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Page<ComputerDTO>>(pageImpl, HttpStatus.OK);
	}

	/**
	 * Résultat recherche des compagnies par nom.
	 * @param search la recherche (required).
	 * @param numeropage le numero de la page à afficher (required).
	 * @param limit nombres de résultats par bloc
	 * @return la réponse en json.
	 */
	@GetMapping(value= "/" + UrlParams.FILTER, params={UrlParams.SEARCH})
	public ResponseEntity<Page<ComputerDTO>> searchPage(@RequestParam(name=UrlParams.SEARCH, required=true) String search,
			@RequestParam(name=UrlParams.PAGE, required=false) Integer iNumpage, 
			@RequestParam(name=UrlParams.LIMIT,  required=false) Integer paramLimit,
			@RequestParam(name = UrlParams.ORDER, required=false) String order) {
		PageComputerOrder pageOrder = (order == null) ? PageComputerOrder.BY_NAME  : PageComputerOrder.valueOf(order);
		if (!SecurityTextValidation.valideString(search)) {
			throw new IllegalSearchException();
		}
		int limit = (paramLimit == null) ? DefaultValues.DEFAULT_LIMIT : paramLimit;
		int iPage = (iNumpage == null) ? DefaultValues.DEFAULT_PAGE : iNumpage;
		Page<Computer> page = null;
		Page<ComputerDTO> pageImpl = null;
		try {
			page = serviceComputer.getPageSearch(search, iPage, limit, pageOrder);
			List<ComputerDTO> list = page.getContent().stream().map(c -> MapperComputer.map(c).get()).collect(Collectors.toList());
			pageImpl = new PageImpl<ComputerDTO>(list, page.getPageable(), page.getTotalElements());
		} catch (ServiceException e) {
			throw new WebServiceException(e);
		}
		return new ResponseEntity<Page<ComputerDTO>>(pageImpl, HttpStatus.OK);
	}

	/**
	 * Suppression des computers.
	 * @param deletes l'id des computers à supprimer.
	 * @return la réponse en json.
	 */
	@DeleteMapping
	public ResponseEntity<Long> delete(@RequestParam(name = UrlParams.DELETE_SELECT, required = true) Set<Long> deletes) {
		Long elements = 0L;
		try {
			elements = serviceComputer.deleteAll(deletes);
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
	public ResponseEntity<Long> add(@RequestBody @Valid ComputerDTO computer) {
		Optional<Computer> c = MapperComputer.map(computer);
		Long res = 0L;
		if (c.isPresent()) {
			try {
				res = serviceComputer.create(c.get());
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
	public ResponseEntity<Boolean> edit(@RequestBody @Valid ComputerDTO computer) {
		Optional<Computer> c = MapperComputer.map(computer);
		Boolean res = false;
		if (c.isPresent()) {
			try {
				res = serviceComputer.update(c.get());
			} catch (ServiceException e) {
				throw new WebServiceException(e);
			}
		} else {
			return new ResponseEntity<Boolean>(res, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Boolean>(res, HttpStatus.OK);
	}

}
