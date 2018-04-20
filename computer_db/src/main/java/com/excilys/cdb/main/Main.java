package com.excilys.cdb.main;

import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.service.ComputerServiceImpl;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.ui.UIController;

public class Main {

	public static void main(String[] args) {
		try {
			IComputerService service = ComputerServiceImpl.getInstance();
			UIController controller = new UIController(service);
			while(true) {
				controller.read();
			}
		} catch (DAOConfigurationException e) {
			System.out.println(e);
		}
	}	

}
