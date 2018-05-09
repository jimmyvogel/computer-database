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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDTO;
import com.excilys.cdb.mapper.PageMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Dao;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.persistence.exceptions.DaoException;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validator.DateValidation;

/**
 * Class du composant Servlet.
 * @author vogel
 *
 */
public class ComputerServlet extends HttpServlet {

    private static final long serialVersionUID = 8303946492164443679L;

    private ComputerService service;

    public enum Action {
        HOME(CHEMIN_ACCUEIL),
        ADD_COMPUTER(CHEMIN_FORM_ADD_COMPUTER),
        EDIT_COMPUTER(CHEMIN_FORM_EDIT_COMPUTER),
        SEARCH_COMPUTER(CHEMIN_LIST_COMPUTERS),
        SEARCH_COMPANY(CHEMIN_LIST_COMPANIES),
        DELETE_COMPUTER(CHEMIN_LIST_COMPUTERS),
        ADD_FORM_COMPUTER(CHEMIN_FORM_ADD_COMPUTER),
        EDIT_FORM_COMPUTER(CHEMIN_FORM_EDIT_COMPUTER),
        LIST_COMPUTERS(CHEMIN_LIST_COMPUTERS),
        LIST_COMPANIES(CHEMIN_LIST_COMPANIES);

        private String vue;

        /**
         * Constructor.
         * @param vue
         *            value de l'enum
         */
        Action(String vue) {
            this.vue = vue;
        }

        public String getVue() {
            return vue;
        }

        public void setVue(String vue) {
            this.vue = vue;
        }
    }

    //Navigation
    private static final String CHEMIN_ACCUEIL = "/WEB-INF/accueil.jsp";
    private static final String CHEMIN_FORM_ADD_COMPUTER = "/forms/formAjoutComputer.jsp";
    private static final String CHEMIN_FORM_EDIT_COMPUTER = "/forms/formEditComputer.jsp";
    private static final String CHEMIN_LIST_COMPUTERS = "/views/listeComputers.jsp";
    private static final String CHEMIN_LIST_COMPANIES = "/views/listeCompanies.jsp";

    private static final String SESSION_ETAT = "etat";
    private static final String SESSION_SEARCH = "search";
    private static final String SESSION_LIMIT_ID = "LimitCompanies";

    private static final String ERROR = "error";
    private static final String SUCCESS = "success";


    @Override
    public void init(ServletConfig config) throws ServletException {
        Logger logger = LoggerFactory.getLogger(ComputerServlet.class);
        logger.info("Configuration de la servlet ComputerServlet en cours.");
        super.init(config);
        try {
            service = ComputerService.getInstance();
        } catch (DAOConfigurationException e) {
            throw new ServletException("Instanciation du service fail", e);
        }
        logger.info("Configuration de la servlet ComputerServlet effectué.");
    }

    /**
     * Récupération des requêtes en GET.
     * @param request la requête
     * @param response la reponse
     * @throws ServletException exception de réception
     * @throws IOException exception de lecture
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("action") != null) {
            Action action = Action.valueOf(request.getParameter("action").toUpperCase());
            try {
                switch (action) {
                case ADD_FORM_COMPUTER:
                    request.setAttribute("companies", service.getAllCompany());
                    break;
                case EDIT_FORM_COMPUTER:
                    addParamsEditComputer(request);
                    break;
                case SEARCH_COMPUTER:
                    addParamsSearchComputer(request);
                    break;
                case SEARCH_COMPANY:
                    addParamsSearchCompany(request);
                    break;
                case LIST_COMPUTERS:
                    addParamsListComputers(request);
                    break;
                case LIST_COMPANIES:
                    addParamsListCompanies(request);
                    break;
                default:
                    dispatch(request, response, CHEMIN_ACCUEIL);
                    break;
                }
                dispatch(request, response, action.getVue());

            } catch (ServiceException | DaoException e) {
                request.setAttribute(ERROR, e.getMessage());
                dispatch(request, response, CHEMIN_ACCUEIL);
            }

        } else {
            dispatch(request, response, CHEMIN_ACCUEIL);
        }
    }

    /**
     * Récupération des requêtes en POST.
     * @param request la requête
     * @param response la reponse
     * @throws ServletException exception de réception
     * @throws IOException exception de lecture
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("action") != null) {
            Action action = Action.valueOf(request.getParameter("action").toUpperCase());
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
                    dispatch(request, response, CHEMIN_ACCUEIL);
                    break;
                }
            } catch (ServiceException | DaoException e) {
                request.setAttribute(ERROR, e.getMessage());
            }
            dispatch(request, response, action.getVue());
        } else {
            dispatch(request, response, CHEMIN_ACCUEIL);
        }
    }

    /**
     * Méthode utile pour récupérer la limit de pagination spécifier dans la request.
     * @param request requête.
     * @return la limit en format primitive int.
     */
    private int getLimitPagination(HttpServletRequest request) {
        // Valeur par défaut si rien de spécifié.
        Integer limit = Dao.LIMIT_DEFAULT;

        // Modification de la limite
        if (request.getParameter("limit") != null) {
            limit = Integer.valueOf(request.getParameter("limit"));
            request.getSession().setAttribute(SESSION_LIMIT_ID, limit);

        // Verification de la limite en session.
        } else if (request.getSession().getAttribute(SESSION_LIMIT_ID) != null) {
            limit = (Integer) request.getSession() .getAttribute(SESSION_LIMIT_ID);
        }
        return limit;
    }

    /**
     * Méthode utile pour récupérer le paramètre search possiblement en session.
     * @param request requête.
     * @return un string représentant la recherche.
     */
    private String getSearch(HttpServletRequest request) {
        // Valeur par défaut si rien de spécifié.
        String search = "";

        // Modification du search
        if (request.getParameter("search") != null) {
            search = request.getParameter("search");
            request.getSession().setAttribute(SESSION_SEARCH, search);

        // Verification de la recherche en session.
        } else if (request.getSession().getAttribute(SESSION_SEARCH) != null) {
            search = (String) request.getSession() .getAttribute(SESSION_SEARCH);
        }
        return search;
    }

    /**
     * Affichage de la recherche des companies.
     * @param request injections et parametres
     * @throws DaoException erreur de requête.
     * @throws ServiceException erreur de service.
     */
    private void addParamsSearchCompany(HttpServletRequest request) throws ServiceException, DaoException {
        int value = 1;
        if (request.getParameter("page") != null) {
            value = Integer.valueOf(request.getParameter("page"));
        }
        Integer limit = getLimitPagination(request);

        Page<Company> page = service.getPageSearchCompany(this.getSearch(request), value, limit);
        request.setAttribute("actionPagination", Action.SEARCH_COMPANY.toString());
        request.setAttribute("page", page);
    }

    /**
     * Affichage de la recherche des computers.
     * @param request injections et parametres
     * @throws DaoException erreur de requête.
     * @throws ServiceException erreur de service.
     */
    private void addParamsSearchComputer(HttpServletRequest request) throws ServiceException, DaoException {
        int value = 1;
        if (request.getParameter("page") != null) {
            value = Integer.valueOf(request.getParameter("page"));
        }

        Integer limit = getLimitPagination(request);

        Page<Computer> page = service.getPageSearchComputer(this.getSearch(request), value, limit);
        Page<ComputerDTO> newpage = PageMapper.mapPageComputerToDto(page);

        request.setAttribute("actionPagination", Action.SEARCH_COMPUTER.toString());
        request.setAttribute("page", newpage);
    }

    /**
     * Suppression d'un computer et injection.
     * @param request injections et parametres
     * @throws DaoException erreur de requête.
     * @throws ServiceException erreur de service.
     */
    private void deleteComputer(HttpServletRequest request) throws DaoException, ServiceException {
        String[] selections = request.getParameter("selection").split(",");
        Set<Long> set = Arrays.stream(selections).map(l -> Long.valueOf(l)).collect(Collectors.toSet());
        service.deleteComputers(set);
        request.setAttribute(SUCCESS, "Delete computer " + Arrays.toString(selections) + " success.");
        addParamsListComputers(request);
    }

    /**
     * Modification d'un computer et injection.
     * @param request injections et parametres
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de requête.
     */
    private void editComputer(HttpServletRequest request)
            throws ServiceException, DaoException {
        long id = Long.valueOf(request.getParameter("id"));

        String name = null;
        String nameS = request.getParameter("computerName");
        if (nameS != null && nameS != "") {
            name = request.getParameter("computerName").trim();
        }
        long idCompany = -1;
        LocalDateTime introduced = null;
        LocalDateTime discontinued = null;
        String introducedS = request.getParameter("introduced");
        String discontinuedS = request.getParameter("discontinued");
        String idS = request.getParameter("companyId");

        if (introducedS != null && introducedS != "") {
            introduced = DateValidation.validationDate(introducedS);
        }

        if (discontinuedS != null && discontinuedS != "") {
            discontinued = DateValidation.validationDate(discontinuedS);
        }
        if (idS != null && idS != "") {
            idCompany = Long.valueOf(idS);
        }
        service.updateComputer(id, name, introduced, discontinued, idCompany);

        request.setAttribute(SUCCESS, "Update Computer success.");
        addParamsEditComputer(request);
    }

    /**
     * Ajout d'un computer, injection et redirection.
     * @param request injections et parametres
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    private void addComputer(HttpServletRequest request)
            throws ServiceException, DaoException {
        String name = request.getParameter("computerName").trim();
        long id = -1;
        LocalDateTime introduced = null;
        LocalDateTime discontinued = null;
        String introducedS = request.getParameter("introduced");
        String discontinuedS = request.getParameter("discontinued");
        String idS = request.getParameter("companyId");

        if (introducedS != null && introducedS != "") {
            introduced = DateValidation.validationDate(introducedS);
        }

        if (discontinuedS != null && discontinuedS != "") {
            discontinued = DateValidation.validationDate(discontinuedS);
        }

        if (idS != null && idS != "") {
            id = Long.valueOf(idS);
        }
        service.createComputer(name, introduced, discontinued, id);

        request.setAttribute(SUCCESS, "Create Computer " + name + " success.");
    }

    /**
     * Gestion d'injection avant redirection pour la page editComputer.
     * @param request injections et parametres
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de requête.
     */
    private void addParamsEditComputer(HttpServletRequest request)
            throws ServiceException, DaoException {
        long id = Long.valueOf(request.getParameter("id"));
        request.setAttribute("computer",
                new ComputerDTO(service.getComputer(id)));
        request.setAttribute("companies", service.getAllCompany());
    }

    /**
     * Gestion d'injection avant redirection pour la page listCompanies.
     * @param request modification des injections
     * @throws ServiceException erreur de service.
     */
    private void addParamsListCompanies(HttpServletRequest request)
            throws ServiceException {
        int value = 1;
        if (request.getParameter("page") != null) {
            value = Integer.valueOf(request.getParameter("page"));
        }

        Integer limit = getLimitPagination(request);
        Page<Company> page = service.getPageCompany(value, limit);
        request.setAttribute("actionPagination", Action.LIST_COMPANIES.toString());
        request.setAttribute("page", page);
    }

    /**
     * Gestion d'injection des dtos computers avant redirection pour la page listComputers.
     * @param request modification des injections
     * @throws ServiceException erreur de service.
     */
    private void addParamsListComputers(HttpServletRequest request)
            throws ServiceException {
        int value = 1;
        if (request.getParameter("page") != null) {
            value = Integer.valueOf(request.getParameter("page"));
        }
        Integer limit = getLimitPagination(request);
        Page<Computer> page = service.getPageComputer(value, limit);
        Page<ComputerDTO> newpage = PageMapper.mapPageComputerToDto(page);

        request.setAttribute("actionPagination", Action.LIST_COMPUTERS.toString());
        request.setAttribute("page", newpage);
    }

    /**
     * Redirection vers la page avec insertion d'un fragment.
     * @param request la requête
     * @param response la reponse
     * @param chemin chemin vers le contenu
     * @throws ServletException exception de réception
     * @throws IOException exception de lecture
     */
    private void dispatch(HttpServletRequest request,
            HttpServletResponse response, String chemin)
            throws ServletException, IOException {
        request.setAttribute(SESSION_ETAT, request.getParameter("action"));
        this.getServletContext().getRequestDispatcher(chemin).forward(request, response);
    }

}
