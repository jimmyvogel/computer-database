package com.excilys.cdb.validator;

import java.time.LocalDate;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.validator.exceptions.ValidatorDateException;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Validator for the instance variables of a computer. Return exceptions when
 * fail.
 * @author vogel
 *
 */
public class ComputerValidator {

	/**
	 * Valid an instance of computer.
	 * @param computer the computer to certifiate.
	 * @return Computer the computer certified.
	 * @throws ValidatorStringException erreur sur un parametre String
	 * @throws ValidatorDateException erreur sur un parametre de type LocalDateTime
	 */
	public static Computer validComputer(Computer computer) throws ValidatorStringException, ValidatorDateException {
		validName(computer.getName());
		validIntroduced(computer.getIntroduced());
		validDiscontinued(computer.getDiscontinued());
		if (computer.getDiscontinued() != null && computer.getIntroduced() == null) {
			throw new ValidatorDateException(ValidatorDateException.TypeError.SECOND_DATE_ALONE);
		}
		if (computer.getIntroduced() != null && computer.getDiscontinued() != null
				&& computer.getIntroduced().isAfter(computer.getDiscontinued())) {
			throw new ValidatorDateException(ValidatorDateException.TypeError.SECOND_DATE_AFTER_FAIL);
		}
		return computer;
	}

	/**
	 * Validator for instance variable name of a computer.
	 * @param name the variable to test
	 * @return String contenant le nom valid avec possible traitement.
	 * @throws ValidatorStringException lance une exception si erreur sur le name.
	 */
	public static String validName(String name) throws ValidatorStringException {
		if (name == null) {
			throw new ValidatorStringException(ValidatorStringException.TypeError.NULL_STRING);
		}
		if (!SecurityTextValidation.valideString(name)) {
			throw new ValidatorStringException(ValidatorStringException.TypeError.ILLEGAL_CHARACTERS);
		}
		if (name.length() < Computer.TAILLE_MIN_NAME || name.length() > Computer.TAILLE_MAX_NAME) {
			throw new ValidatorStringException(ValidatorStringException.TypeError.BAD_LENGTH);
		}
		return name;
	}

	/**
	 * Validator for date introduced of a computer.
	 * @param introduced date à vérifier.
	 * @return LocalDateTime date valide.
	 * @throws ValidatorDateException lance une exception si erreur sur la date.
	 */
	public static LocalDate validIntroduced(final LocalDate introduced) throws ValidatorDateException {
		if (introduced != null) {
			if (!DateValidation.validDateInBetween(introduced, Computer.BEGIN_DATE_VALID, Computer.END_DATE_VALID)) {
				throw new ValidatorDateException(ValidatorDateException.TypeError.ILLEGAL_DATE);
			}
		}
		return introduced;
	}

	/**
	 * Validator for date discontinued of a computer.
	 * @param discontinued date à vérifier.
	 * @return LocalDateTime date valide.
	 * @throws ValidatorDateException lance une exception si erreur sur la date.
	 */
	public static LocalDate validDiscontinued(final LocalDate discontinued) throws ValidatorDateException {
		if (discontinued != null) {
			if (!DateValidation.validDateInBetween(discontinued, Computer.BEGIN_DATE_VALID, Computer.END_DATE_VALID)) {
				throw new ValidatorDateException(ValidatorDateException.TypeError.ILLEGAL_DATE_2);
			}
		}
		return discontinued;
	}

}
