package main.java.com.excilys.cdb.main;

import main.java.com.excilys.cdb.ui.UIController;

public class Main {

	public static void main(String[] args) {
		UIController controller = new UIController();
		while(true) {
			controller.read();
		}
	}	

}
