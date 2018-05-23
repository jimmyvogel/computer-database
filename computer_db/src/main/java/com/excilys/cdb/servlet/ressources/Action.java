package com.excilys.cdb.servlet.ressources;

import com.excilys.cdb.servlet.ComputerController;
import com.excilys.cdb.servlet.CompanyController;

public enum Action {
	HOME(DefaultValues.HOME),
	ADD_COMPUTER(ComputerController.ADD_COMPUTER),
	EDIT_COMPUTER(ComputerController.EDIT_COMPUTER),
	SEARCH_COMPUTER(ComputerController.SEARCH_COMPUTER),
	DELETE_COMPUTER(ComputerController.DELETE_COMPUTER),
	ADD_FORM_COMPUTER(ComputerController.ADD_FORM_COMPUTER),
	EDIT_FORM_COMPUTER(ComputerController.EDIT_FORM_COMPUTER),
	LIST_COMPUTERS(ComputerController.LIST_COMPUTERS),
	LIST_COMPANIES(CompanyController.LIST_COMPANIES),
	SEARCH_COMPANY(CompanyController.SEARCH_COMPANY);

	private final String value;

	/**
	 * Constructor.
	 * @param value value de l'enum
	 */
	Action(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}