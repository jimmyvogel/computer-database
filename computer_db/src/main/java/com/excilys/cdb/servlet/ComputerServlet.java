package com.excilys.cdb.servlet;

import java.io.IOException;
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
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.service.ComputerServiceImpl;
import com.excilys.cdb.service.exceptions.ServiceException;

/**
 * Class du composant Servlet.
 * @author vogel
 *
 */
public class ComputerServlet extends HttpServlet {

    private static final long serialVersionUID = 8303946492164443679L;

    private ComputerServiceImpl service;

    public enum Action {
        HOME(CHEMIN_ACCUEIL),
        ADD_COMPUTER(CHEMIN_ADD_COMPUTER),
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
    private static final String CHEMIN_ADD_COMPUTER = "/forms/formAjoutComputer.jsp";
    private static final String CHEMIN_LIST_COMPUTERS = "/views/listeComputers.jsp";
    private static final String CHEMIN_LIST_COMPANIES = "/views/listeCompanies.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        Logger logger = LoggerFactory.getLogger(ComputerServlet.class);
        logger.info("Configuration de la servlet ComputerServlet en cours.");
        super.init(config);
        try {
            service = ComputerServiceImpl.getInstance();
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

        if (request.getParameter("action") != null) {
            Action action = Action.valueOf(request.getParameter("action"));
            try {
                switch (action) {
                    case HOME: break;
                    case ADD_COMPUTER:
                        request.setAttribute("companies", service.getAllCompany());
                        break;
                    case LIST_COMPUTERS:
                        addParamsListComputers(request);
                        break;
                    case LIST_COMPANIES:
                        request.setAttribute("companies", service.getAllCompany());
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
     * Gestion d'injection avant redirection pour la page listComputers.
     * @param request modification des injections
     * @throws ServiceException erreur de service.
     */
    private void addParamsListComputers(HttpServletRequest request) throws ServiceException {
        int value = 1;
        if (request.getParameter("page") != null) {
            value = Integer.valueOf(request.getParameter("page"));
        }

        List<ComputerDTO> computersDtos = new ArrayList<>();
        Page<Computer> page = service.getPageComputer(value);
        for (Computer c : page.getObjects()) {
            computersDtos.add(new ComputerDTO(c));
        }
        request.setAttribute("computers", computersDtos);
        request.setAttribute("nbComputers", page.getCount());
        request.setAttribute("maxPage", page.getMaxPage());
        request.setAttribute("pages", page.pageRestantesInList(5));
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
        this.getServletContext().getRequestDispatcher(CHEMIN_ACCUEIL)
                .forward(request, response);
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
