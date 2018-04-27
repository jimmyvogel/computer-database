package com.excilys.cdb.util;

import java.io.IOException;
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
                printUrl(out, target, action, page.getPageCourante() - 1);
                out.print("<li><a href=\"urlPagerTag\" aria-label=\"Previous\">");
                out.print("<span aria-hidden=\"true\">&laquo;</span></a></li>");
            }

            @SuppressWarnings("unchecked")
            List<Integer> list = page.pageRestantesInList(5);

            for (Integer i : list) {
                printUrl(out, target, action, i);
                out.print("<li><a href=\"urlPagerTag\">" + i + "</a></li>");
            }

            //right possible
            if (page.getPageCourante() < page.getMaxPage()) {
                printUrl(out, target, action, page.getPageCourante() + 1);
                out.print("<li><a href=\"urlPagerTag\" aria-label=\"Next\">");
                out.print("<span aria-hidden=\"true\">&raquo;</span></a></li>");
            }
            out.print("</ul>");
            out.print("</div>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SKIP_BODY;
    }

    /**
     * Ecriture de l'url de redirection.
     * @param out le writer jsp
     * @param target le controlleur
     * @param action l'action
     * @param page la page a spécfié.
     * @throws IOException exception
     */
    private void printUrl(JspWriter out, String target, String action, int page) throws IOException {
        out.print("<c:url value=\"" + target + "\" var=\"urlPagerTag\">");
        out.print("<c:param name=\"action\" value=\"" + action + "\"/>");
        out.print("<c:param name=\"page\" value=\"" + page + "\"/>");
        out.print("</c:url>");
    }

}
