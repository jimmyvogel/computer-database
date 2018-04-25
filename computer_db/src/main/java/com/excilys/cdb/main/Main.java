package com.excilys.cdb.main;

import com.excilys.cdb.persistence.exceptions.DAOConfigurationException;
import com.excilys.cdb.service.ComputerServiceImpl;
import com.excilys.cdb.service.IComputerService;
import com.excilys.cdb.ui.UIController;

/**
 * Class main.
 * @author vogel
 *
 */
final class Main {

    /**
     * Constructor.
     *
     */
    private Main() {
    }

    /**
     * Main.
     * @param args arguments
     */
    public static void main(final String[] args) {
        try {
            IComputerService service = ComputerServiceImpl.getInstance();
            UIController controller = new UIController(service);
            controller.run();
        } catch (DAOConfigurationException e) {
            System.out.println(e);
        }
    }

}
