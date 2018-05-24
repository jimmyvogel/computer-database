package com.excilys.cdb.util;

import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.excilys.cdb.persistence.Page;


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


    public void setPage(@SuppressWarnings("rawtypes") Page page) {
        this.page = page;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public void setTarget(String target) {
        this.target = target;
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
            if (page.getPageCourante() > 1) {
                out.print("<li><a href=\"" + getUrl(1, page.getLimit()) + "\" aria-label=\"First\">");
                out.print("<span aria-hidden=\"true\">&laquo;</span></a></li>");
                out.print("<li><a href=\"" + getUrl(page.getPageCourante() - 1, page.getLimit()) + "\" aria-label=\"Previous\">");
                out.print("<span aria-hidden=\"true\"><</span></a></li>");
            }

            @SuppressWarnings("unchecked")
            List<Integer> list = page.pageRestantesInList(5);

            for (Integer i : list) {
                if (i == page.getPageCourante()) {
                    out.print("<li class=\"active\"><a href=\"" + getUrl(i, page.getLimit()) + "\">" + i + "</a></li>");
                } else {
                    out.print("<li><a href=\"" + getUrl(i, page.getLimit()) + "\">" + i + "</a></li>");
                }
            }

            //right possible
            if (page.getPageCourante() < page.getMaxPage()) {
                out.print("<li><a href=\"" + getUrl(page.getPageCourante() + 1, page.getLimit()) + "\" aria-label=\"Next\">");
                out.print("<span aria-hidden=\"true\">></span></a></li>");
                out.print("<li><a href=\"" + getUrl(page.getMaxPage(), page.getLimit()) + "\" aria-label=\"Last\">");
                out.print("<span aria-hidden=\"true\">&raquo;</span></a></li>");
            }
            out.print("</ul><br>");
            List<Integer> limites = Arrays.asList(10, 20, 50, 100);
            out.print("<ul class=\"pagination\">");
            for (Integer limit : limites) {
                if (limit.equals(page.getLimit())) {
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
    	if (target == null) {
    		return action + "?page=" + page + "&limit=" + limit;
    	}
        return target + "/" + action + "?page=" + page + "&limit=" + limit;
    }

}
