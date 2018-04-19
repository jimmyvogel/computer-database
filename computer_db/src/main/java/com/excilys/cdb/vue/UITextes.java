package main.java.com.excilys.cdb.vue;

/**
 * Ressources de types String incorporés dans la vue.
 * 
 * @author vogel
 *
 */
public class UITextes {

	public static final String MENU_INITIAL = 
			"Choisir Action \n" +
			"1-List computers\n" + 
			"2-List companies\n" + 
			"3-Show computer details \n" + 
			"4-Create a computer\n" + 
			"5-Update a computer\n" + 
			"6-Delete a computer";
	
	public static final String RETOUR =
			"Pour revenir, tappez r";
	
	public static final String LIST_PAGINATION =
			"Tapez un chiffre pour aller sur une autre page";
	
	public static final String VALIDATION =
			"Valider (yes/no) \n";
	
	public static final String UPDATE_ID =
			"Choisissez l'id du computer à modifier :";
	
	public static final String UPDATE_NAME =
			"Choisissez le nouveau nom :";
	
	public static final String UPDATE_INTRODUCED =
			"Choisissez la nouvelle date[yyyy-mm-dd] d'introduction (passer->no):";
	
	public static final String UPDATE_DISCONTINUED =
			"Choisissez la nouvelle date[yyyy-mm-dd] d'arrêt (passer->no):";
	
	public static final String UPDATE_COMPANY_ID =
			"Choisissez une nouvelle company par id(passer->no):";
	
	
	public static final String AJOUT_NAME =
			"Choisissez le nom (obligatoire):";
	
	public static final String AJOUT_INTRODUCED =
			"Choisissez la date[yyyy-mm-dd] d'introduction (optionnel->no):";
	
	public static final String AJOUT_DISCONTINUED =
			"Choisissez la date[yyyy-mm-dd] d'arrêt (optionnel->no):";
	
	public static final String AJOUT_COMPANY_ID =
			"Choisissez l'id de la company (optionnel->no):";
}
