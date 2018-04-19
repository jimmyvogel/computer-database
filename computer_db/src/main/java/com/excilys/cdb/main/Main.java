package main.java.com.excilys.cdb.main;

import main.java.com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import main.java.com.excilys.cdb.service.ComputerServiceImpl;
import main.java.com.excilys.cdb.service.IComputerService;
import main.java.com.excilys.cdb.ui.UIController;

public class Main {

	public static void main(String[] args) {
		try {
			IComputerService service = new ComputerServiceImpl();
			UIController controller = new UIController(service);
			while(true) {
				controller.read();
			}
		} catch (DAOConfigurationException e) {
			System.out.println(e);
		}
	}	

}
