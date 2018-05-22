package com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.PageMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.DateValidation;

/**
 * Class du composant Servlet.
 * @author vogel
 *
 */
@SuppressWarnings("serial")
public class ComputerServlet extends HttpServlet {

	@Autowired
	private ComputerService serviceComputer;
	@Autowired
	private CompanyService serviceCompany;

	public enum Action {
		HOME(UrlRessources.ACCUEIL),
		ADD_COMPUTER(UrlRessources.FORM_ADD_COMPUTER),
		EDIT_COMPUTER(UrlRessources.FORM_EDIT_COMPUTER),
		SEARCH_COMPUTER(UrlRessources.LIST_COMPUTERS),
		DELETE_COMPUTER(UrlRessources.LIST_COMPUTERS),
		ADD_FORM_COMPUTER(UrlRessources.FORM_ADD_COMPUTER),
		EDIT_FORM_COMPUTER(UrlRessources.FORM_EDIT_COMPUTER),
		LIST_COMPUTERS(UrlRessources.LIST_COMPUTERS);

		private String url;

		/**
		 * Constructor.
		 * @param url value de l'enum
		 */
		Action(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	private static final String ALL_COMPANY = "companies";
	private static final String COMPUTER_ID = "id";
	private static final String COMPUTER = "computer";

	/**
	 * Récupération des requêtes en GET.
	 * @param request
	 *            la requête
	 * @param response
	 *            la reponse
	 * @throws ServletException
	 *             exception de réception
	 * @throws IOException
	 *             exception de lecture
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter(RequestID.ACTION) != null) {
			Action action = Action.valueOf(request.getParameter(RequestID.ACTION).toUpperCase());
			try {
				switch (action) {
				case ADD_FORM_COMPUTER:
					request.setAttribute(ALL_COMPANY, serviceCompany.getAll());
					break;
				case EDIT_FORM_COMPUTER:
					addParamsEditComputer(request);
					break;
				case SEARCH_COMPUTER:
					addParamsSearchComputer(request);
					break;
				case LIST_COMPUTERS:
					addParamsListComputers(request);
					break;
				default:
					dispatch(request, response, UrlRessources.ACCUEIL);
					break;
				}
				dispatch(request, response, action.getUrl());

			} catch (ServiceException | DaoException e) {
				request.setAttribute(JspRessources.ERROR, e.getMessage());
				dispatch(request, response, UrlRessources.ACCUEIL);
			}

		} else {
			dispatch(request, response, UrlRessources.ACCUEIL);
		}
	}

	/**
	 * Récupération des requêtes en POST.
	 * @param request
	 *            la requête
	 * @param response
	 *            la reponse
	 * @throws ServletException
	 *             exception de réception
	 * @throws IOException
	 *             exception de lecture
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter(RequestID.ACTION) != null) {
			Action action = Action.valueOf(request.getParameter(RequestID.ACTION).toUpperCase());
			try {
				switch (action) {
				case ADD_COMPUTER:
					addComputer(request);
					break;
				case EDIT_COMPUTER:
					editComputer(request);
					break;
				case DELETE_COMPUTER:
					deleteComputer(request);
					break;
				default:
					dispatch(request, response, UrlRessources.ACCUEIL);
					break;
				}
			} catch (ServiceException | DaoException e) {
				request.setAttribute(JspRessources.ERROR, e.getMessage());
			}
			dispatch(request, response, action.getUrl());
		} else {
			dispatch(request, response, UrlRessources.ACCUEIL);
		}
	}

	/**
	 * Affichage de la recherche des computers.
	 * @param request
	 *            injections et parametres
	 * @throws DaoException
	 *             erreur de requête.
	 * @throws ServiceException
	 *             erreur de service.
	 */
	private void addParamsSearchComputer(HttpServletRequest request) throws ServiceException, DaoException {
		int value = 1;
		if (request.getParameter(RequestID.PAGE) != null) {
			value = Integer.valueOf(request.getParameter(RequestID.PAGE));
		}

		Integer limit = Session.getLimitPagination(request);

		Page<Computer> page = serviceComputer.getPageSearch(Session.getSearch(request), value, limit);
		Page<ComputerDTO> newpage = PageMapper.mapPageComputerToDto(page);

		request.setAttribute(RequestID.ACTION_PAGINATION, Action.SEARCH_COMPUTER.toString());
		request.setAttribute(RequestID.PAGE, newpage);
	}

	/**
	 * Suppression d'un computer et injection.
	 * @param request
	 *            injections et parametres
	 * @throws DaoException
	 *             erreur de requête.
	 * @throws ServiceException
	 *             erreur de service.
	 */
	private void deleteComputer(HttpServletRequest request) throws DaoException, ServiceException {
		String[] selections = request.getParameter(RequestID.DELETE_SELECT).split(",");
		Set<Long> set = Arrays.stream(selections).map(l -> Long.valueOf(l)).collect(Collectors.toSet());
		serviceComputer.deleteAll(set);
		request.setAttribute(JspRessources.SUCCESS, "Delete computer " + Arrays.toString(selections) + " success.");
		addParamsListComputers(request);
	}

	/**
	 * Modification d'un computer et injection.
	 * @param request
	 *            injections et parametres
	 * @throws ServiceException
	 *             erreur de service.
	 * @throws DaoException
	 *             erreur de requête.
	 */
	private void editComputer(HttpServletRequest request) throws ServiceException, DaoException {
		long id = Long.valueOf(request.getParameter(COMPUTER_ID));

		String name = null;
		String nameS = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_NAME);
		if (nameS != null && nameS != "") {
			name = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_NAME).trim();
		}
		long idCompany = -1;
		LocalDateTime introduced = null;
		LocalDateTime discontinued = null;
		String introducedS = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_INTRODUCED);
		String discontinuedS = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_DISCONTINUED);
		String idS = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY);

		if (introducedS != null && introducedS != "") {
			introduced = DateValidation.validationDate(introducedS);
		}

		if (discontinuedS != null && discontinuedS != "") {
			discontinued = DateValidation.validationDate(discontinuedS);
		}
		if (idS != null && idS != "") {
			idCompany = Long.valueOf(idS);
		}
		serviceComputer.update(id, name, introduced, discontinued, idCompany);

		request.setAttribute(JspRessources.SUCCESS, "Update Computer success.");
		addParamsEditComputer(request);
	}

	/**
	 * Ajout d'un computer, injection et redirection.
	 * @param request
	 *            injections et parametres
	 * @throws ServiceException
	 *             erreur de service.
	 * @throws DaoException
	 *             erreur de reqûete.
	 */
	private void addComputer(HttpServletRequest request) throws ServiceException, DaoException {
		String name = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_NAME).trim();
		long id = -1;
		LocalDateTime introduced = null;
		LocalDateTime discontinued = null;
		String introducedS = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_INTRODUCED);
		String discontinuedS = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_DISCONTINUED);
		String idS = request.getParameter(JspRessources.FORM_COMPUTER_PARAM_IDCOMPANY);

		if (introducedS != null && introducedS != "") {
			introduced = DateValidation.validationDate(introducedS);
		}

		if (discontinuedS != null && discontinuedS != "") {
			discontinued = DateValidation.validationDate(discontinuedS);
		}

		if (idS != null && idS != "") {
			id = Long.valueOf(idS);
		}
		serviceComputer.create(name, introduced, discontinued, id);

		request.setAttribute(JspRessources.SUCCESS, "Create Computer " + name + " success.");
	}

	/**
	 * Gestion d'injection avant redirection pour la page editComputer.
	 * @param request
	 *            injections et parametres
	 * @throws ServiceException
	 *             erreur de service.
	 * @throws DaoException
	 *             erreur de requête.
	 */
	private void addParamsEditComputer(HttpServletRequest request) throws ServiceException, DaoException {
		long id = Long.valueOf(request.getParameter(COMPUTER_ID));
		request.setAttribute(COMPUTER, new ComputerDTO(serviceComputer.get(id)));
		request.setAttribute(ALL_COMPANY, serviceCompany.getAll());
	}

	/**
	 * Gestion d'injection des dtos computers avant redirection pour la page
	 * listComputers.
	 * @param request
	 *            modification des injections
	 * @throws ServiceException
	 *             erreur de service.
	 */
	private void addParamsListComputers(HttpServletRequest request) throws ServiceException {
		int value = 1;
		if (request.getParameter(RequestID.PAGE) != null) {
			value = Integer.valueOf(request.getParameter(RequestID.PAGE));
		}
		Integer limit = Session.getLimitPagination(request);
		Page<Computer> page = serviceComputer.getPage(value, limit);
		Page<ComputerDTO> newpage = PageMapper.mapPageComputerToDto(page);

		request.setAttribute(RequestID.ACTION_PAGINATION, Action.LIST_COMPUTERS.toString());
		request.setAttribute(RequestID.PAGE, newpage);
	}

	/**
	 * Redirection vers la page avec insertion d'un fragment.
	 * @param request
	 *            la requête
	 * @param response
	 *            la reponse
	 * @param chemin
	 *            chemin vers le contenu
	 * @throws ServletException
	 *             exception de réception
	 * @throws IOException
	 *             exception de lecture
	 */
	private void dispatch(HttpServletRequest request, HttpServletResponse response, String chemin)
			throws ServletException, IOException {
		request.setAttribute(Session.SESSION_ETAT, request.getParameter(RequestID.ACTION));
		this.getServletContext().getRequestDispatcher(chemin).forward(request, response);
	}

}
