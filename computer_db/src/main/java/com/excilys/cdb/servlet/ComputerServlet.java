package com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDTO;
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
        ADD_COMPUTER(CHEMIN_ADD_COMPUTER),
        EDIT_COMPUTER(CHEMIN_EDIT_COMPUTER),
        ADD_FORM_COMPUTER(CHEMIN_FORM_ADD_COMPUTER),
        EDIT_FORM_COMPUTER(CHEMIN_FORM_EDIT_COMPUTER),
        LIST_COMPUTERS(CHEMIN_LIST_COMPUTERS),
        LIST_COMPANIES(CHEMIN_LIST_COMPANIES);

        private String vue;

        /**
         * Constructor.
         * @param vue value de l'enum
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
    private static final String CHEMIN_ACCUEIL = "/WEB-INF/accueil.jsp";
    private static final String CHEMIN_FORM_ADD_COMPUTER = "/forms/formAjoutComputer.jsp";
    private static final String CHEMIN_FORM_EDIT_COMPUTER = "/forms/formEditComputer.jsp";
    private static final String CHEMIN_ADD_COMPUTER = "/forms/formAjoutComputer.jsp";
    private static final String CHEMIN_EDIT_COMPUTER = "/forms/formEditComputer.jsp";
    private static final String CHEMIN_LIST_COMPUTERS = "/views/listeComputers.jsp";
    private static final String CHEMIN_LIST_COMPANIES = "/views/listeCompanies.jsp";

    private static final String SESSION_LIMIT_COMPUTERS_ID = "LimitComputers";
    private static final String SESSION_LIMIT_COMPANIES_ID = "LimitCompanies";
    private static final Integer DEFAULT_LIMIT = Dao.LIMIT_DEFAULT;

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
     * @param request
     *            la requête
     * @param response
     *            la reponse
     * @throws ServletException
     *             exception de réception
     * @throws IOException
     *             exception de lecture
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("Utilisation du doGet");
        if (request.getParameter("action") != null) {
            Action action = Action.valueOf(request.getParameter("action"));
            try {
                switch (action) {
                    case ADD_FORM_COMPUTER:
                        request.setAttribute("companies", service.getAllCompany());
                        break;
                    case EDIT_FORM_COMPUTER:
                        addParamsEditComputer(request);
                    case LIST_COMPUTERS:
                        addParamsListComputers(request);
                        break;
                    case LIST_COMPANIES:
                        addParamsListCompanies(request);
                        break;
                    default: dispatch(request, response, CHEMIN_ACCUEIL); break;
                }
                dispatch(request, response, action.getVue());

            } catch (ServiceException e) {
                dispatch(request, response, CHEMIN_ACCUEIL);
            }

        } else {
            dispatch(request, response, CHEMIN_ACCUEIL);
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
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("action") != null) {
            Action action = Action.valueOf(request.getParameter("action"));
            try {
                switch (action) {
                    case ADD_COMPUTER:
                        addComputer(request);
                        break;
                    case EDIT_COMPUTER:
                        editComputer(request);
                        break;
                    default: dispatch(request, response, CHEMIN_ACCUEIL); break;
                }
                dispatch(request, response, action.getVue());

            } catch (ServiceException | DaoException e) {
                System.out.println(e.getMessage());
                dispatch(request, response, CHEMIN_ACCUEIL);
            }

        } else {
            dispatch(request, response, CHEMIN_ACCUEIL);
        }
    }

    /**
     * Modification d'un computer, injection et redirection.
     * @param request injections et parametres
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de requête.
     */
    private void editComputer(HttpServletRequest request) throws ServiceException, DaoException {
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

        if (introducedS != null  && introducedS != "") {
            introduced = DateValidation.validationDate(introducedS);
        }

        if (discontinuedS != null  && discontinuedS != "") {
            discontinued = DateValidation.validationDate(discontinuedS);
        }
        if (idS != null && idS != "") {
            idCompany = Long.valueOf(idS);
        }
        System.out.println(introducedS);
        System.out.println(discontinuedS);
        System.out.println(introduced);
        System.out.println(discontinued);
        System.out.println(idS);
        System.out.println(id);
        System.out.println(service.updateComputer(id, name, introduced, discontinued, idCompany));
    }

    /**
     * Ajout d'un computer, injection et redirection.
     * @param request injections et parametres
     * @throws ServiceException erreur de service.
     * @throws DaoException erreur de reqûete.
     */
    private void addComputer(HttpServletRequest request) throws ServiceException, DaoException {
        String name = request.getParameter("computerName").trim();
        long id = -1;
        LocalDateTime introduced = null;
        LocalDateTime discontinued = null;
        String introducedS = request.getParameter("introduced");
        String discontinuedS = request.getParameter("discontinued");
        String idS = request.getParameter("companyId");

        if (introducedS != null  && introducedS != "") {
            introduced = DateValidation.validationDate(introducedS);
        }

        if (discontinuedS != null  && discontinuedS != "") {
            discontinued = DateValidation.validationDate(discontinuedS);
        }

        if (idS != null && idS != "") {
            id = Long.valueOf(idS);
        }
        System.out.println(introducedS);
        System.out.println(discontinuedS);
        System.out.println(introduced);
        System.out.println(discontinued);
        System.out.println(idS);
        System.out.println(id);
        System.out.println(service.createComputer(name, introduced, discontinued, id));
    }

    /**
     * Gestion d'injection avant redirection pour la page editComputer.
     * @param request injections et parametres
     * @throws ServiceException erreur de service.
     */
    private void addParamsEditComputer(HttpServletRequest request) throws ServiceException {
        long id = Long.valueOf(request.getParameter("id"));
        request.setAttribute("computer", new ComputerDTO(service.getComputer(id)));
        request.setAttribute("companies", service.getAllCompany());
    }

    /**
     * Gestion d'injection avant redirection pour la page listCompanies.
     * @param request modification des injections
     * @throws ServiceException erreur de service.
     */
    private void addParamsListCompanies(HttpServletRequest request) throws ServiceException {
        int value = 1;
        if (request.getParameter("page") != null) {
            value = Integer.valueOf(request.getParameter("page"));
        }

        //Valeur par défaut si rien de spécifié.
        Integer limit = DEFAULT_LIMIT;

        //Modification de la limite
        if (request.getParameter("limit") != null) {
            System.out.println("paremeter limit vue");
            limit = Integer.valueOf(request.getParameter("limit"));
            request.getSession().setAttribute(SESSION_LIMIT_COMPANIES_ID, limit);
            value = Integer.valueOf(request.getParameter("page"));

        //Verification de la limite en session.
        } else if (request.getSession().getAttribute(SESSION_LIMIT_COMPANIES_ID) != null) {

            System.out.println("paremeter get Attribute vue");
            limit = (Integer) request.getSession().getAttribute(SESSION_LIMIT_COMPANIES_ID);
        }

        System.out.println(limit);
        Page<Company> page = service.getPageCompany(value, limit);
        request.setAttribute("page", page);
    }

    /**
     * Gestion d'injection avant redirection pour la page listComputers.
     * @param request modification des injections
     * @throws ServiceException erreur de service.
     */
    private void addParamsListComputers(HttpServletRequest request) throws ServiceException {
        int value = 1;
        if (request.getParameter("page") != null) {
            value = Integer.valueOf(request.getParameter("page"));
        }

        //Valeur par défaut si rien de spécifié.
        Integer limit = DEFAULT_LIMIT;

        //Modification de la limite
        if (request.getParameter("limit") != null) {
            limit = Integer.valueOf(request.getParameter("limit"));
            request.getSession().setAttribute(SESSION_LIMIT_COMPUTERS_ID, limit);
            value = Integer.valueOf(request.getParameter("page"));

        //Verification de la limite en session.
        } else if (request.getSession().getAttribute(SESSION_LIMIT_COMPUTERS_ID) != null) {
            limit = (Integer) request.getSession().getAttribute(SESSION_LIMIT_COMPUTERS_ID);
        }

        //Recuperation des computers et transformation en dtos.
        List<ComputerDTO> computersDtos = new ArrayList<>();
        Page<Computer> page = service.getPageComputer(value, limit);
        for (Computer c : page.getObjects()) {
            computersDtos.add(new ComputerDTO(c));
        }

        //Creation de la copie de la page avec chargement des dtos plutôt que des entities.
        Page<ComputerDTO> newpage = new Page<>(page.getLimit(), page.getCount());
        newpage.charge(computersDtos, page.getPageCourante());

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
        this.getServletContext().getRequestDispatcher(chemin)
        .forward(request, response);
    }

}
