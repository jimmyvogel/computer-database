package main.java.com.excilys.cdb.ui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.ComputerServiceImpl;
import main.java.com.excilys.cdb.service.IComputerService;
import main.java.com.excilys.cdb.vue.UITextes;
import main.java.com.excilys.cdb.vue.UIView;

public class UIController {

	private IComputerService service = new ComputerServiceImpl();
	
	private enum Menu { INITIAL, FORM_UPDATE, FORM_AJOUT, DELETE, SHOW, RETOUR};
	private enum Update { NONE, ID, NAME, INTRODUCED, DISCONTINUED, COMPANY_ID, VALIDATE};
	private enum Ajout { NONE, NAME, INTRODUCED, DISCONTINUED, COMPANY_ID, VALIDATE };
	
	private Scanner scanner = new Scanner(System.in);
	private Menu state;
	private Computer inter;
	private Update stateUpdate;
	private Ajout stateAjout;
	
	private UIView view;
	
	public UIController() {
		state = Menu.INITIAL;
		stateUpdate = Update.NONE;
		stateAjout = Ajout.NONE;
		view = new UIView(UITextes.MENU_INITIAL);
	}
	
	public void read() {
		System.out.println("passe dans le read");
		view.display();
		String ligne = scanner.nextLine();
		interprate(ligne);
	}
	
	private void interprate(String ligne) {
		if(ligne.trim().equals("r")) {
			if(state==Menu.RETOUR || state==Menu.DELETE || state==Menu.SHOW)
				view.setAffichage(UITextes.MENU_INITIAL);
			actionRetour();
		}else {
			long value;
			switch(state) {
				case INITIAL:
					int choix = Integer.valueOf(ligne);
					switch(choix) {
						case 1: 
							view.setAffichage(displayListComputers());
							state = Menu.RETOUR; break;
						case 2: 
							view.setAffichage(displayListCompanies());
							state = Menu.RETOUR; break;
						case 3: 
							view.setAffichage("Choissisez l'id.");
							state = Menu.SHOW; break;
						case 4: 
							state = Menu.FORM_AJOUT; 
							view.setAffichage(UITextes.AJOUT_NAME);
							break;
						case 5: 
							state = Menu.FORM_UPDATE; 
							view.setAffichage(UITextes.UPDATE_ID);
							break;
						case 6: 
							view.setAffichage("Choissisez l'id:");
							state = Menu.DELETE;
							break;
						default:break;
					}
					break;
				case FORM_AJOUT:
					switch(stateAjout) {
						case NAME: 
							inter = new Computer();
							inter.setName(ligne); 
							view.setAffichage(UITextes.AJOUT_INTRODUCED);
							break;
						case INTRODUCED: 
							if(!ligne.equals("no"))
								inter.setIntroduced(traitementDate(ligne));
							view.setAffichage(UITextes.AJOUT_DISCONTINUED);
							break;
						case DISCONTINUED: 
							if(!ligne.equals("no"))
								inter.setDiscontinued(traitementDate(ligne)); 
							view.setAffichage(UITextes.AJOUT_COMPANY_ID);
							break;
						case COMPANY_ID:
							if(!ligne.equals("no")) {
								Company c = service.getCompany(Integer.valueOf(ligne));
								inter.setCompany(c); 
							}
							view.setAffichage(UITextes.VALIDATION);
							break;
						case VALIDATE: 
							if(!ligne.equals("yes"))
								view.setAffichage(UITextes.MENU_INITIAL);
							else
								view.setAffichage(ajouterComputer()+"\n"+UITextes.MENU_INITIAL);
							break;
						default:break;
					}break;
				case FORM_UPDATE: 
					switch(stateUpdate) {
						case ID:
							value = Long.valueOf(ligne);
							if(service.getComputer(value)!=null) {
								inter = new Computer();
								inter.setId(value);
								view.setAffichage(UITextes.UPDATE_NAME);
							}else {
								//Quitte si erreur;
								stateUpdate = Update.VALIDATE;
							}
							break;
						case NAME: 
							if(!ligne.equals("no"))
								inter.setName(ligne); 
							view.setAffichage(UITextes.UPDATE_INTRODUCED);break;
						case INTRODUCED: 
							if(!ligne.equals("no"))
								inter.setIntroduced(traitementDate(ligne));
							view.setAffichage(UITextes.UPDATE_DISCONTINUED);
							break;
						case DISCONTINUED: 
							if(!ligne.equals("no"))
								inter.setDiscontinued(traitementDate(ligne)); 
							view.setAffichage(UITextes.UPDATE_COMPANY_ID);break;
						case COMPANY_ID: 
							if(!ligne.equals("no")) {
								Company c = service.getCompany(Integer.valueOf(ligne));
								inter.setCompany(c); 
							}
							view.setAffichage(UITextes.VALIDATION);
							break;
						case VALIDATE: 
							if(!ligne.equals("yes"))
								view.setAffichage(UITextes.MENU_INITIAL);
							else
								view.setAffichage(updateComputer()+"\n"+UITextes.MENU_INITIAL);
							break;
						default:break;
					}break;
				case SHOW:
					value = Long.valueOf(ligne);
					view.setAffichage(detailComputer(value));
					break;
				case DELETE:
					value = Long.valueOf(ligne);
					view.setAffichage(supprimerComputer(value));
					break;
				default: break;
			}
			actionAvance();
		}
		System.out.println(state);
		System.out.println(stateAjout);
		System.out.println(stateUpdate);
		if(state != Menu.INITIAL) {
			view.setAffichage(view.getAffichage()+"["+UITextes.RETOUR+"]");
		}
	}
	
	private void actionRetour() {
		switch(state) {
			case INITIAL:break;
			case FORM_AJOUT:
				switch(stateAjout) {
					case NONE: break;
					case NAME: 
						stateAjout = Ajout.NONE; 
						state = Menu.INITIAL; 
						break;
					case INTRODUCED: stateAjout = Ajout.NAME; break;
					case DISCONTINUED: stateAjout = Ajout.INTRODUCED; break;
					case COMPANY_ID: stateAjout = Ajout.DISCONTINUED; break;
					default: break;
				}break;
			case FORM_UPDATE: 
				switch(stateUpdate) {
					case NONE:
						break;
					case ID: 
						stateUpdate = Update.NONE; 
						state = Menu.INITIAL;
						break;
					case NAME: stateUpdate = Update.ID; break;
					case INTRODUCED: stateUpdate = Update.NAME; break;
					case DISCONTINUED: stateUpdate = Update.INTRODUCED; break;
					case COMPANY_ID: stateUpdate = Update.DISCONTINUED; break;
					default: break;
				}break;
			case RETOUR:state = Menu.INITIAL; break;
			case DELETE:state = Menu.INITIAL; break;
			case SHOW: state = Menu.INITIAL; break;
		}
	}
	
	private void actionAvance() {
		switch(state) {
			case INITIAL:break;
			case FORM_AJOUT:
				switch(stateAjout) {
					case NONE: stateAjout = Ajout.NAME; break;
					case NAME: stateAjout = Ajout.INTRODUCED; break;
					case INTRODUCED: stateAjout = Ajout.DISCONTINUED; break;
					case DISCONTINUED: stateAjout = Ajout.COMPANY_ID; break;
					case COMPANY_ID: stateAjout = Ajout.VALIDATE; break;
					case VALIDATE:
						state = Menu.INITIAL;
						stateAjout = Ajout.NONE;
						break;
					default: break;
				}break;
			case FORM_UPDATE: 
				switch(stateUpdate) {
					case NONE: stateUpdate = Update.ID; break;
					case ID: stateUpdate = Update.NAME; break;
					case NAME: stateUpdate = Update.INTRODUCED; break;
					case INTRODUCED: stateUpdate = Update.DISCONTINUED; break;
					case DISCONTINUED: stateUpdate = Update.COMPANY_ID; break;
					case COMPANY_ID: stateUpdate = Update.VALIDATE; break;
					case VALIDATE:
						state = Menu.INITIAL;
						stateUpdate = Update.NONE; 
						break;
					default: break;
				}break;
			case RETOUR:break;
			default: break;
		}
	}
	
	private String displayListCompanies() {
		List<Company> companies = service.getAllCompany();
		String affichage="Result:\n";
		for(Company c : companies)
			affichage += c.getId() + " " + c.getName() + " \n ";
		return affichage;
	}
	
	private String displayListComputers() {
		List<Computer> computers = service.getAllComputer();
		String affichage="Result:\n";
		for(Computer c : computers)
			affichage += c.getId() + " " + c.getName() + " \n ";
		return affichage;
	}
	
	private String ajouterComputer() {
		long id = -1;
		if(inter.getCompany()!=null)
			id= inter.getCompany().getId();
		boolean ajout = 
				service.createComputer(inter.getName(), inter.getIntroduced(), 
						inter.getDiscontinued(), id);
		
		if(ajout)
			return "Ajout réussit\n";
		
		return "Ajout fail\n";
	}
	
	private String supprimerComputer(long id) {
		boolean delete = 
				service.deleteComputer(id);
		
		if(delete)
			return "Suppression réussi\n";
		
		return "Suppression fail\n";		
	}
	
	private String updateComputer() {
		long id = -1;
		if(inter.getCompany()!=null)
			id = inter.getCompany().getId();
		
		boolean update = 
				service.updateComputer(inter.getId(), inter.getName(), 
						inter.getIntroduced(), inter.getDiscontinued(), 
						id);
		if(update)
			return "Update réussit\n";
		
		return "Update fail\n";
	}
	
	private String detailComputer(long id) {
		Computer c = service.getComputer(id);
		if(c==null)return "Erreur d'id";
		
		String affichage="Computer: " + id + "\n";
		affichage += c.getName() + " \n ";
		if(c.getIntroduced()!=null)
			affichage += c.getIntroduced() + " \n ";
		if(c.getDiscontinued()!=null)
			affichage += c.getDiscontinued() + " \n ";
		if(c.getCompany()!=null)
			affichage += c.getCompany().getName() + "\n ";
		return affichage;
	}
	
	private LocalDateTime traitementDate(String ligne) {
		return LocalDateTime.now();
	}
	
	
	
}
