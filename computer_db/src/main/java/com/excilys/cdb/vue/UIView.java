package main.java.com.excilys.cdb.vue;

public class UIView {

	private String affichage;
	
	public UIView(String initial) {
		affichage = initial;
	}

	public String getAffichage() {
		return affichage;
	}

	public void setAffichage(String affichage) {
		this.affichage = affichage;
	}
	
	public void display() {
		System.out.println(affichage);
	}
	
	
}
