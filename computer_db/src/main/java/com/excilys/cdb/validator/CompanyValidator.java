package com.excilys.cdb.validator;

import java.util.Arrays;
import java.util.Collections;

import com.excilys.cdb.exception.MessageHandler;
import com.excilys.cdb.exception.MessageHandler.CDBMessage;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Validator for instance variables of a company.
 * @author vogel
 *
 */
public class CompanyValidator {

	/**
	 * Validation of parameter name.
	 * @param name string to validate
	 * @return String the valid name
	 * @throws ValidatorStringException exception sur le name.
	 */
	public static String validName(final String name) throws ValidatorStringException {
		if (name == null) {
			throw new ValidatorStringException(MessageHandler.getMessage(CDBMessage.VALIDATION_NAME_NULL,
					Collections.singleton(name).toArray()));
		}
		if (name.length() < Company.TAILLE_MIN_NAME || name.length() > Company.TAILLE_MAX_NAME) {
			throw new ValidatorStringException(MessageHandler.getMessage(CDBMessage.VALIDATION_NAME_LENGTH,
					Arrays.asList(Company.TAILLE_MIN_NAME, Company.TAILLE_MAX_NAME).toArray()));
		}
		return name;
	}

}
