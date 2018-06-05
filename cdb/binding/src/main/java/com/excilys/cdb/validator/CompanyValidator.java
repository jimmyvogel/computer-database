package com.excilys.cdb.validator;

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
			throw new ValidatorStringException(ValidatorStringException.TypeError.NULL_STRING);
		}
		if (name.length() < Company.TAILLE_MIN_NAME || name.length() > Company.TAILLE_MAX_NAME) {
			throw new ValidatorStringException(ValidatorStringException.TypeError.BAD_LENGTH);
		}
		return name;
	}

}
