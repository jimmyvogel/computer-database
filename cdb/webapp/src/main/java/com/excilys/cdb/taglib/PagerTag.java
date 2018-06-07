package com.excilys.cdb.taglib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.data.domain.Page;


/**
 * @author SergeyZ
 */
public class PagerTag extends TagSupport {

    /**
     */
    private static final long serialVersionUID = -3073077658952397365L;

    @SuppressWarnings("rawtypes")
    private Page page;
    private String action;
    private String target;
    private String params;
    private Integer position;


    public void setPage(@SuppressWarnings("rawtypes") Page page) {
        this.page = page;
        position = page.getNumber() + 1;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * Méthode d'éxécution du tag.
     * @return int -
     * @throws JspException jspException
     */
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.print("<div class=\"container text-center\">");
            out.print("<ul class=\"pagination\">");
            //left possible
            if (position > 1) {
                out.print("<li><a href=\"" + getUrl(1, page.getSize()) + "\" aria-label=\"First\">");
                out.print("<span aria-hidden=\"true\">&laquo;</span></a></li>");
                out.print("<li><a href=\"" + getUrl(position - 1, page.getSize()) + "\" aria-label=\"Previous\">");
                out.print("<span aria-hidden=\"true\"><</span></a></li>");
            }

            List<Integer> list = pageRestantesInList(5);

            for (Integer i : list) {
                if (i == position) {
                    out.print("<li class=\"active\"><a href=\"" + getUrl(i, page.getSize()) + "\">" + i + "</a></li>");
                } else {
                    out.print("<li><a href=\"" + getUrl(i, page.getSize()) + "\">" + i + "</a></li>");
                }
            }

            //right possible
            if (position < page.getTotalPages()) {
                out.print("<li><a href=\"" + getUrl(position + 1, page.getSize()) + "\" aria-label=\"Next\">");
                out.print("<span aria-hidden=\"true\">></span></a></li>");
                out.print("<li><a href=\"" + getUrl(page.getTotalPages(), page.getSize()) + "\" aria-label=\"Last\">");
                out.print("<span aria-hidden=\"true\">&raquo;</span></a></li>");
            }
            out.print("</ul><br>");
            List<Integer> limites = Arrays.asList(10, 20, 50, 100);
            out.print("<ul class=\"pagination\">");
            for (Integer limit : limites) {
                if (limit.equals(page.getSize())) {
                    out.print("<li class=\"active\"><a href=\"" + getUrl(1, limit) + "\">" + limit + "</a></li>");
                } else {
                    out.print("<li><a href=\"" + getUrl(1, limit) + "\">" + limit + "</a></li>");
                }
            }
            out.print("</ul>");
            out.print("</div>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SKIP_BODY;
    }

    /**
     * Format de l'url en GET with limit.
     * @param page le numero de la page
     * @param limit rajout de la limit dans la requête
     * @return le format sous forme d'un string
     */
    private String getUrl(int page, int limit) {
    	String t = target == null ? "" : target + "/";
    	String p = params == null ? "" : "?" + params;
    	String numpage = params == null ? ("?page=" + page) : ("&page=" + page);
        return t + action + p + numpage + "&limit=" + limit;
    }
    
	/**
	 * Retourne une liste incrémenté à partir de la pageCourante pour un nombre
	 * d'itérations donné.
	 * @param nombreElements le nombre d'éléments maximum à insérer
	 * @return une liste contenant la pagination demandé
	 */
	public List<Integer> pageRestantesInList(int nombreElements) {
		List<Integer> pages = new ArrayList<Integer>();

		// Moins un pour gérer l'ajout de la pageCourante.
		int moitier = (nombreElements - 1) / 2;
		int reste = (nombreElements - 1) - moitier;

		// La moitié des éléments à gauche de la pageCourante, le reste à droite.
		int first = (position - moitier <= 0) ? 1 : (position - moitier);
		int last = (position + reste > page.getTotalPages()) ? page.getTotalPages() : (position + reste);

		for (int i = first; i <= last; i++) {
			pages.add(i);
		}

		return pages;
	}

}
