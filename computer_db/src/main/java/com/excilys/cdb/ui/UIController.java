package com.excilys.cdb.ui;

import java.time.LocalDateTime;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.Page;
import com.excilys.cdb.service.ComputerServiceImpl;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.service.exceptions.ServiceException;
import com.excilys.cdb.validation.DateValidation;
import com.excilys.cdb.vue.UITextes;
import com.excilys.cdb.vue.UIView;

/** Controler de l'interface cli.
 * @author vogel
 *
 */
public class UIController {

    // Le service de gestion des computers et des compagnies.
    private IComputerService service;

    // Les énumérations permettant la gestion comme un automate du processus.
    private enum State {
        INITIAL, LIST_COMPANY, LIST_COMPUTER, FORM_UPDATE, FORM_AJOUT, DELETE, SHOW, RETOUR
    };

    private enum Update {
        NONE, ID, NAME, INTRODUCED, DISCONTINUED, COMPANY_ID, VALIDATE
    };

    private enum Ajout {
        NONE, NAME, INTRODUCED, DISCONTINUED, COMPANY_ID, VALIDATE
    };

    // Le scanner de lecture
    private Scanner scanner = new Scanner(System.in);

    // Les états actuels.
    private State state;
    private Update stateUpdate;
    private Ajout stateAjout;

    // Les pages sur lesquels on est actuellement
    private int numPageComputer = 1;
    private int numPageCompany = 1;

    // La vue
    private UIView view;

    // Un objet de type computer pour avoir en mémoire les dernières demandes.
    private Computer inter;

    /**
     * Constructor sans arguments initiant les variables d'états, la vue et les
     * pageurs.
     * @param computerService le singleton service.
     */
    public UIController(final IComputerService computerService) {
        Logger logger = LoggerFactory.getLogger(ComputerServiceImpl.class);
        logger.info("Création d'un objet de type UIController");

        this.service = computerService;
        initialize();
    }

    /** Méthode private pour intialisé les variables de l'automate.
     */
    private void initialize() {
        state = State.INITIAL;
        stateUpdate = Update.NONE;
        stateAjout = Ajout.NONE;
        view = new UIView(UITextes.MENU_INITIAL + " \n[stop->quit]");
    }

    /**
     * Lancement de l'ui.
     */
    public void run() {
        boolean continu = true;
        while (continu) {
            continu = read();
        }
    }

    /**
     * Le pas de lecture de l'automate.
     * @return un boolean pour dire si on continue.
     */
    private boolean read() {
        boolean continu = true;
        try {
            view.display();
            String ligne = scanner.nextLine();
            if (ligne.equals("quit") || ligne.equals("stop")) {
                return false;
            }

            try {
                interprate(ligne);
            } catch (ServiceException e) {
                initialize();
                view.setAffichage("Erreur de la dao \n" + view.getAffichage());
            }
        } catch (java.lang.NumberFormatException e) {
            view.setAffichage(
                    "Erreur, rentrée un chiffre \n" + view.getAffichage());
        }
        return continu;
    }

    /**
     * L'interprétation des résultats.
     * @param ligne le string a interprété
     * @throws ServiceException exception de service
     */
    private void interprate(final String ligne) throws ServiceException {

        // Un retour a été demandé.
        if (ligne.trim().equals("r")) {

            // Si on est pas sur un retour pour l'ajout ou l'update, rafficher
            // le menu principal.
            if (state == State.RETOUR || state == State.LIST_COMPANY
                    || state == State.LIST_COMPUTER || state == State.DELETE
                    || state == State.SHOW || stateAjout == Ajout.NONE
                    || stateUpdate == Update.NONE) {
                view.setAffichage(UITextes.MENU_INITIAL + " \n[stop->quit]");
            }

            // AUTOMATE RETOUR
            actionRetour();

        } else {

            long value;
            // Selon l'état de l'automate.
            switch (state) {

            // Gestion des choix sur le menu initial.
            case INITIAL:
                int choix = Integer.valueOf(ligne);
                this.switchPrincipal(choix);
                break;

            // Gestion des choix si on est dans le formulaire d'ajout.
            case FORM_AJOUT:
                this.switchFormulaireAjout(ligne);
                break;

            // Gestion des choix si on est dans le formulaire d'update.
            case FORM_UPDATE:
                this.switchFormulaireUpdate(ligne);
                break;

            // Gestion des choix si on est dans l'affichage des compagnies.
            case LIST_COMPANY:
                value = Long.valueOf(ligne);
                numPageCompany = (int) value;
                view.setAffichage(pageurCompanyShow(numPageCompany));
                break;

            // Gestion des choix si on est dans l'affichage des computers.
            case LIST_COMPUTER:
                value = Long.valueOf(ligne);
                numPageComputer = (int) value;
                view.setAffichage(pageurComputerShow(numPageComputer));
                break;

            // Gestion des choix si on est dans l'affichage des détails d'un
            // computer
            case SHOW:
                value = Long.valueOf(ligne);
                view.setAffichage(detailComputer(value));
                break;

            // Gestion des choix si onest dans l'affichage des détails d'une
            // compagnie.
            case DELETE:
                value = Long.valueOf(ligne);
                view.setAffichage(supprimerComputer(value));
                break;
            default:
                break;
            }

            // AUTOMATE AVANCE
            actionAvance();
        }

        // Si l'état est différent du menu initial on rajoute la possibilité du
        // retour.
        if (state != State.INITIAL) {
            view.setAffichage(
                    view.getAffichage() + " [" + UITextes.RETOUR + "]");
        }
        view.setAffichage(view.getAffichage() + " \n[stop->quit]");

    }

    /**
     * Gestion du retour en arrière de l'automate.
     */
    private void actionRetour() {
        switch (state) {
        case INITIAL:
            break;
        case FORM_AJOUT:
            switch (stateAjout) {
            case NONE:
                break;
            case NAME:
                stateAjout = Ajout.NONE;
                state = State.INITIAL;
                break;
            case INTRODUCED:
                stateAjout = Ajout.NAME;
                break;
            case DISCONTINUED:
                stateAjout = Ajout.INTRODUCED;
                break;
            case COMPANY_ID:
                stateAjout = Ajout.DISCONTINUED;
                break;
            default:
                break;
            }
            break;
        case FORM_UPDATE:
            switch (stateUpdate) {
            case NONE:
                break;
            case ID:
                stateUpdate = Update.NONE;
                state = State.INITIAL;
                break;
            case NAME:
                stateUpdate = Update.ID;
                break;
            case INTRODUCED:
                stateUpdate = Update.NAME;
                break;
            case DISCONTINUED:
                stateUpdate = Update.INTRODUCED;
                break;
            case COMPANY_ID:
                stateUpdate = Update.DISCONTINUED;
                break;
            default:
                break;
            }
            break;

        // Par défaut on revient au menu initial.
        default:
            state = State.INITIAL;
            break;
        }
    }

    /**
     * Gestion du pas en avant de l'automate.
     */
    private void actionAvance() {
        switch (state) {
        case INITIAL:
            break;
        case FORM_AJOUT:
            switch (stateAjout) {
            case NONE:
                stateAjout = Ajout.NAME;
                break;
            case NAME:
                stateAjout = Ajout.INTRODUCED;
                break;
            case INTRODUCED:
                stateAjout = Ajout.DISCONTINUED;
                break;
            case DISCONTINUED:
                stateAjout = Ajout.COMPANY_ID;
                break;
            case COMPANY_ID:
                stateAjout = Ajout.VALIDATE;
                break;
            case VALIDATE:
                state = State.INITIAL;
                stateAjout = Ajout.NONE;
                break;
            default:
                break;
            }
            break;
        case FORM_UPDATE:
            switch (stateUpdate) {
            case NONE:
                stateUpdate = Update.ID;
                break;
            case ID:
                stateUpdate = Update.NAME;
                break;
            case NAME:
                stateUpdate = Update.INTRODUCED;
                break;
            case INTRODUCED:
                stateUpdate = Update.DISCONTINUED;
                break;
            case DISCONTINUED:
                stateUpdate = Update.COMPANY_ID;
                break;
            case COMPANY_ID:
                stateUpdate = Update.VALIDATE;
                break;
            case VALIDATE:
                state = State.INITIAL;
                stateUpdate = Update.NONE;
                break;
            default:
                break;
            }
            break;
        // Par défaut on ne bouge pas.
        default:
            break;
        }
    }

    /** Gestion du switch sur le menu initial pour l'interpretation.
     * @param choix
     *            choix sous la forme d'un int.
     * @throws ServiceException exception de service
     */
    private void switchPrincipal(final int choix) throws ServiceException {
        switch (choix) {
        case 1:
            view.setAffichage(pageurComputerShow(1));
            state = State.LIST_COMPUTER;
            break;
        case 2:
            view.setAffichage(pageurCompanyShow(1));
            state = State.LIST_COMPANY;
            break;
        case 3:
            view.setAffichage("Choissisez l'id.");
            state = State.SHOW;
            break;
        case 4:
            state = State.FORM_AJOUT;
            view.setAffichage(UITextes.AJOUT_NAME);
            break;
        case 5:
            state = State.FORM_UPDATE;
            view.setAffichage(UITextes.UPDATE_ID);
            break;
        case 6:
            view.setAffichage("Choissisez l'id:");
            state = State.DELETE;
            break;
        default:
            break;
        }
    }

    /**
     * Gestion du switch sur le formulaire d'ajout pour l'interpretation.
     * @param ligne
     *            input de l'utilisateur
     * @throws ServiceException exception de service
     */
    private void switchFormulaireAjout(String ligne) throws ServiceException {
        LocalDateTime timeInter;
        switch (stateAjout) {
        case NAME:
            inter = new Computer();
            inter.setName(ligne);
            view.setAffichage(UITextes.AJOUT_INTRODUCED);
            break;
        case INTRODUCED:
            if (!ligne.equals("no")) {
                timeInter = DateValidation.validationDate(ligne);
                if (timeInter == null) {
                    view.setAffichage("Error" + UITextes.AJOUT_INTRODUCED);
                    stateAjout = Ajout.NAME;
                    break;
                }
                inter.setIntroduced(timeInter);
            }
            view.setAffichage(UITextes.AJOUT_DISCONTINUED);
            break;
        case DISCONTINUED:
            if (!ligne.equals("no")) {
                timeInter = DateValidation.validationDate(ligne);
                if (timeInter == null) {
                    view.setAffichage("Error" + UITextes.AJOUT_DISCONTINUED);
                    stateAjout = Ajout.INTRODUCED;
                    break;
                }
                inter.setDiscontinued(timeInter);
            }
            view.setAffichage(UITextes.AJOUT_COMPANY_ID);
            break;
        case COMPANY_ID:
            if (!ligne.equals("no")) {
                Company c = service.getCompany(Integer.valueOf(ligne));
                inter.setCompany(c);
            }
            view.setAffichage(UITextes.VALIDATION);
            break;
        case VALIDATE:
            if (!ligne.equals("yes")) {
                view.setAffichage(UITextes.MENU_INITIAL);
            } else {
                view.setAffichage(
                        ajouterComputer() + "\n" + UITextes.MENU_INITIAL);
            }
            break;
        default:
            break;
        }
    }

    /**
     * Gestion du formulaire d'update pour l'interpretation.
     * @param ligne
     *            input de l'utilisateur
     * @throws ServiceException exception de service
     * @throws NumberFormatException exception sur le type caster en lecture
     */
    private void switchFormulaireUpdate(String ligne)
            throws NumberFormatException, ServiceException {
        long value;
        LocalDateTime timeInter;
        switch (stateUpdate) {
        case ID:
            value = Long.valueOf(ligne);
            if (service.getComputer(value) != null) {
                inter = new Computer();
                inter.setId(value);
                view.setAffichage(UITextes.UPDATE_NAME);
            } else {
                // Quitte si erreur;
                stateUpdate = Update.VALIDATE;
            }
            break;
        case NAME:
            if (!ligne.equals("no")) {
                inter.setName(ligne);
            }
            view.setAffichage(UITextes.UPDATE_INTRODUCED);
            break;
        case INTRODUCED:
            if (!ligne.equals("no")) {
                timeInter = DateValidation.validationDate(ligne);
                if (timeInter == null) {
                    view.setAffichage("Error" + UITextes.UPDATE_INTRODUCED);
                    stateUpdate = Update.NAME;
                    break;
                }
                inter.setIntroduced(DateValidation.validationDate(ligne));
            }
            view.setAffichage(UITextes.UPDATE_DISCONTINUED);
            break;
        case DISCONTINUED:
            if (!ligne.equals("no")) {
                timeInter = DateValidation.validationDate(ligne);
                if (timeInter != null) {
                    view.setAffichage("Error" + UITextes.UPDATE_DISCONTINUED);
                    stateUpdate = Update.INTRODUCED;
                    break;
                }
                inter.setDiscontinued(DateValidation.validationDate(ligne));
            }
            view.setAffichage(UITextes.UPDATE_COMPANY_ID);
            break;
        case COMPANY_ID:
            if (!ligne.equals("no")) {
                Company c = service.getCompany(Integer.valueOf(ligne));
                inter.setCompany(c);
            }
            view.setAffichage(UITextes.VALIDATION);
            break;
        case VALIDATE:
            if (!ligne.equals("yes")) {
                view.setAffichage(UITextes.MENU_INITIAL);
            } else {
                view.setAffichage(
                        updateComputer() + "\n" + UITextes.MENU_INITIAL);
            }
            break;
        default:
            break;
        }
    }

    /**Ajouter un computer dans la bdd.
     * @return l'affichage du résultat dans un string.
     * @throws ServiceException exception de service
     */
    private String ajouterComputer() throws ServiceException {
        long id = -1;
        if (inter.getCompany() != null) {
            id = inter.getCompany().getId();
        }
        boolean ajout = service.createComputer(inter.getName(),
                inter.getIntroduced(), inter.getDiscontinued(), id) != -1;

        if (ajout) {
            return "Ajout réussit\n";
        }

        return "Ajout fail\n";
    }

    /** Supprimer un computer dans la bdd.
     * @param id
     *            l'id du computer a supprimé
     * @return l'affichage du résultat dans un string.
     * @throws ServiceException exception de service
     */
    private String supprimerComputer(final long id) throws ServiceException {
        boolean delete = service.deleteComputer(id);

        if (delete) {
            return "Suppression réussi\n";
        }

        return "Suppression fail\n";
    }

    /**
     * Modifié un computer dans la bdd.
     * @return l'affichage du résultat dans un string.
     * @throws ServiceException
     *             exception service exception de service
     */
    private String updateComputer() throws ServiceException {
        long id = -1;
        if (inter.getCompany() != null) {
            id = inter.getCompany().getId();
        }

        boolean update = service.updateComputer(inter.getId(), inter.getName(),
                inter.getIntroduced(), inter.getDiscontinued(), id);
        if (update) {
            return "Update réussit\n";
        }

        return "Update fail\n";
    }

    /** Méthode private pour récupérer les données d'une page de
     * computer(pagination).
     * @param page
     *            la page demandé
     * @return l'affichage du résultat dans un string.
     * @throws ServiceException exception service
     */
    private String pageurComputerShow(final int page) throws ServiceException {
        Page<Computer> pageComputer = service.getPageComputer(page);
        int taille = (int) service.countComputers();
        int limit = pageComputer.getLimit();
        int endBloc = taille / limit;
        if (taille % limit > 0) {
            endBloc++;
        }
        return pageComputer.toString() + "\n Page:" + page + "/" + endBloc
                + "\n" + UITextes.LIST_PAGINATION;
    }

    /** Méthode private pour récupérer les données d'une page de
     * company(pagination).
     * @param page
     *            la page demandé
     * @return l'affichage du résultat dans un string.
     * @throws ServiceException exception service
     */
    private String pageurCompanyShow(final int page) throws ServiceException {
        Page<Company> pageCompany = service.getPageCompany(page);
        int taille = (int) service.countCompanies();
        int limit = pageCompany.getLimit();
        int endBloc = taille / limit;
        if (taille % limit > 0) {
            endBloc++;
        }
        return pageCompany.toString() + "\n Page:" + page + "/" + endBloc + "\n"
                + UITextes.LIST_PAGINATION;
    }

    /** Afficher les détails d'un computer.
     * @param id l'id du computer a affiché
     * @return les résultats dans un String.
     * @throws ServiceException exception de service
     */
    private String detailComputer(final long id) throws ServiceException {
        Computer c = service.getComputer(id);
        if (c == null) {
            return "Erreur d'id";
        }

        String affichage = "Computer: " + id + "\n";
        affichage += c.getName() + " \n ";
        if (c.getIntroduced() != null) {
            affichage += c.getIntroduced() + " \n ";
        }
        if (c.getDiscontinued() != null) {
            affichage += c.getDiscontinued() + " \n ";
        }
        if (c.getCompany() != null) {
            affichage += c.getCompany().getName() + "\n ";
        }
        return affichage;
    }

}
