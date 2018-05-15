package com.excilys.cdb.servlet;

import javax.servlet.http.HttpServletRequest;

import com.excilys.cdb.persistence.Dao;

public class Session {

    static final String SESSION_SEARCH = "search";
    static final String SESSION_LIMIT = "limit";
    static final String SESSION_ETAT = "etat";

    /**
     * No constructor.
     */
    private Session() { }

    /**
     * Méthode utile pour récupérer la limit de pagination possiblement en session.
     * @param request requête.
     * @return la limit en format primitive int.
     */
    static int getLimitPagination(HttpServletRequest request) {
        // Valeur par défaut si rien de spécifié.
        Integer limit = Dao.LIMIT_DEFAULT;

        // Modification de la limite
        if (request.getParameter(RequestID.LIMIT) != null) {
            limit = Integer.valueOf(request.getParameter(RequestID.LIMIT));
            request.getSession().setAttribute(SESSION_LIMIT, limit);

        // Verification de la limite en session.
        } else if (request.getSession().getAttribute(SESSION_LIMIT) != null) {
            limit = (Integer) request.getSession().getAttribute(SESSION_LIMIT);
        }
        return limit;
    }

    /**
     * Méthode utile pour récupérer le paramètre search possiblement en session.
     * @param request requête.
     * @return un string représentant la recherche.
     */
    static String getSearch(HttpServletRequest request) {
        // Valeur par défaut si rien de spécifié.
        String search = "";

        // Modification du search
        if (request.getParameter(RequestID.SEARCH) != null) {
            search = request.getParameter(RequestID.SEARCH);
            request.getSession().setAttribute(SESSION_SEARCH, search);

        // Verification de la recherche en session.
        } else if (request.getSession().getAttribute(SESSION_SEARCH) != null) {
            search = (String) request.getSession() .getAttribute(SESSION_SEARCH);
        }
        return search;
    }

}
