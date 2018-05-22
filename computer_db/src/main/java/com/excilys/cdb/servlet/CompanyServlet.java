package com.excilys.cdb.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.exceptions.ServiceException;

/**
 * Class du composant Servlet.
 * @author vogel
 *
 */
@SuppressWarnings("serial")
public class CompanyServlet extends HttpServlet {

	@Autowired
	private CompanyService serviceCompany;

	public enum Action {
		HOME(UrlRessources.ACCUEIL), SEARCH_COMPANY(UrlRessources.LIST_COMPANIES), LIST_COMPANIES(
				UrlRessources.LIST_COMPANIES);
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

	/**
	 * Récupération des requêtes en GET.
	 * @param request la requête
	 * @param response la reponse
	 * @throws ServletException exception de réception
	 * @throws IOException exception de lecture
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter(RequestID.ACTION) != null) {
			Action action = Action.valueOf(request.getParameter(RequestID.ACTION).toUpperCase());
			try {
				switch (action) {
				case SEARCH_COMPANY:
					addParamsSearchCompany(request);
					break;
				case LIST_COMPANIES:
					addParamsListCompanies(request);
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
	 * @param request la requête
	 * @param response la reponse
	 * @throws ServletException exception de réception
	 * @throws IOException exception de lecture
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Affichage de la recherche des companies.
	 * @param request injections et parametres
	 * @throws DaoException erreur de requête.
	 * @throws ServiceException erreur de service.
	 */
	private void addParamsSearchCompany(HttpServletRequest request) throws ServiceException, DaoException {
		int value = 1;
		if (request.getParameter(RequestID.PAGE) != null) {
			value = Integer.valueOf(request.getParameter(RequestID.PAGE));
		}
		Integer limit = Session.getLimitPagination(request);

		Page<Company> page = serviceCompany.getPageSearch(Session.getSearch(request), value, limit);
		request.setAttribute(RequestID.ACTION_PAGINATION, Action.SEARCH_COMPANY.toString());
		request.setAttribute(RequestID.PAGE, page);
	}

	/**
	 * Gestion d'injection avant redirection pour la page listCompanies.
	 * @param request modification des injections
	 * @throws ServiceException erreur de service.
	 */
	private void addParamsListCompanies(HttpServletRequest request) throws ServiceException {
		int value = 1;
		if (request.getParameter(RequestID.PAGE) != null) {
			value = Integer.valueOf(request.getParameter(RequestID.PAGE));
		}

		Integer limit = Session.getLimitPagination(request);
		Page<Company> page = serviceCompany.getPage(value, limit);
		request.setAttribute(RequestID.ACTION_PAGINATION, Action.LIST_COMPANIES.toString());
		request.setAttribute(RequestID.PAGE, page);
	}

	/**
	 * Redirection vers la page avec insertion d'un fragment.
	 * @param request la requête
	 * @param response la reponse
	 * @param chemin chemin vers le contenu
	 * @throws ServletException exception de réception
	 * @throws IOException exception de lecture
	 */
	private void dispatch(HttpServletRequest request, HttpServletResponse response, String chemin)
			throws ServletException, IOException {
		request.setAttribute(Session.SESSION_ETAT, request.getParameter(RequestID.ACTION));
		this.getServletContext().getRequestDispatcher(chemin).forward(request, response);
	}

}
