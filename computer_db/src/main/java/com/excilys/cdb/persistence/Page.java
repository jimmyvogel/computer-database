package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Class pour gérer la pagination des données.
 * @author vogel
 * @param <T> le type de donnée à charger
 */
@SuppressWarnings("serial")
public class Page<T> {

    private List<T> objects;

    //La page courante
    private int pageCourante;

    //Le nombre d'élément chargé.
    private int taille;

    //la taille d'un bloc.
    private int limit;

    //Le nombre d'élément maximum.
    private long count;

    //Max page
    private int maxPage;

    /**
     * Constructor.
     * @param limit le nombre d'objets par bloc
     * @param l le nombre d'éléments en bdd
     */
    public Page(int limit, long l) {
        objects = new ArrayList<T>();
        this.limit = limit;
        this.count = l;
        this.maxPage = (int) Math.ceil((double) l / limit);
    }

    /**
     * Récupérer l'offset d'une page à partir du limit spécifié à la construction.
     * @param numeroPage le numero de la page
     * @return le numero du premier élément
     */
    public int offset(int numeroPage) {
        return (numeroPage - 1) * limit;
    }

    /**
     * Récupérer le nombre de pages restante à partir de la pageCourante.
     * @return le nombre de page restante
     */
    public int pageRestantes() {
        return this.maxPage - pageCourante;
    }

    /**
     * Retourne une liste incrémenté à partir de la pageCourante pour un
     * nombre d'itérations donné.
     * @param nombreElements le nombre d'éléments maximum à insérer
     * @return une liste contenant la pagination demandé
     */
    public List<Integer> pageRestantesInList(int nombreElements) {
        List<Integer> pages = new ArrayList<Integer>();

        //Moins un pour gérer l'ajout de la pageCourante.
        int moitier = (nombreElements - 1) / 2;
        int reste = (nombreElements - 1) - moitier;

        //La moitié des éléments à gauche de la pageCourante, le reste à droite.
        int first = (pageCourante - moitier <= 0) ? 1 : (pageCourante - moitier);
        int last = (pageCourante + reste > this.maxPage) ? this.maxPage : (pageCourante + reste);

        for (int i = first; i <= last; i++) {
            pages.add(i);
        }

        return pages;
    }

    /**
     * Chargement des données.
     * @param objects les objets à charger
     * @param numeroPage la page à charger
     */
    public void charge(List<T> objects, int numeroPage) {
        this.pageCourante = numeroPage;
        if (objects.size() <= limit) {
            taille = objects.size();
            this.objects = objects;
        } else {
            this.objects = objects.subList(0, limit);
        }
    }

    public int getMaxPage() {
        return maxPage;
    }

    public long getCount() {
        return count;
    }

    /**
     * Getter.
     * @return la taille par bloc
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Getter.
     * @return le numero de la page courante.
     */
    public int getPageCourante() {
        return pageCourante;
    }

    /**
     * La taille réel du chargement.
     * @return la taille réel.
     */
    public int getTaille() {
        return taille;
    }

    /**
     * Récuper les données chargés.
     * @return une liste des objets chargés.
     */
    public List<T> getObjects() {
        return objects;
    }

    /**
     * Affichage de la page.
     * @return l'affichage sous forme de string.
     */
    public String toString() {
        String res = "";
        for (T t : objects) {
            res += t.toString() + "\n";
        }
        return res;
    }

}
