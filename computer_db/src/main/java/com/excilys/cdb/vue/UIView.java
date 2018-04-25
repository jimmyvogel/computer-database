package com.excilys.cdb.vue;

/**
 * Classe représentant la vue dans un CLI.
 * @author vogel
 *
 */
public class UIView {

    private String affichage;

    /**
     * Constructor.
     * @param initial parametre d'affichage
     */
    public UIView(final String initial) {
        affichage = initial;
    }

    /**
     * Getteur.
     * @return affichage
     */
    public String getAffichage() {
        return affichage;
    }

    /*
     * Setteur.
     */
    public void setAffichage(final String affichage) {
        this.affichage = affichage;
    }

    /**
     * Afficher la vue.
     */
    public void display() {
        System.out.println(affichage);
    }

}
