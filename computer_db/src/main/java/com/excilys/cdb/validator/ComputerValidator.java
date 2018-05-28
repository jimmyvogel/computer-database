package com.excilys.cdb.validator;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.validator.exceptions.ValidatorDateException;
import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Validator for the instance variables of a computer. Return exceptions when fail.
 * @author vogel
 *
 */
public class ComputerValidator {

    public static final int TAILLE_MIN_NAME = 3;
    public static final int TAILLE_MAX_NAME = 60;

    public static final LocalDateTime BEGIN_DATE_VALID = LocalDateTime.of(1972,
            Month.DECEMBER, 31, 23, 59);

    public static final LocalDateTime END_DATE_VALID = LocalDateTime.of(2030,
            Month.JANUARY, 1, 0, 0);

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

        //Discontinued without introduced
        if (computer.getDiscontinued() != null && computer.getIntroduced() == null) {
            throw new ValidatorDateException("Computer introduced is null and discontinued is not.");
        }

        //Discontinued and introduced but discontinued before
        if (computer.getIntroduced()   != null && computer.getDiscontinued() != null
                && computer.getIntroduced().isAfter(computer.getDiscontinued())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            throw new ValidatorDateException("Computer introduced is after computer discontinued.");
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
            throw new ValidatorStringException("Le nom est null");
        }
    	if (!SecurityTextValidation.valideString(name)) {
    		throw new ValidatorStringException("Le nom contient des characters non valide.");
    	}
        if (name.length() < TAILLE_MIN_NAME) {
            throw new ValidatorStringException("Le nom est trop court " + TAILLE_MIN_NAME + " lettres minimum");
        }
        if (name.length() > TAILLE_MAX_NAME) {
            throw new ValidatorStringException("Le nom est trop long " + TAILLE_MAX_NAME + " lettres maximum");
        }
        return name;
    }

    /**
     * Validator for date introduced of a computer.
     * @param introduced date à vérifier.
     * @return LocalDateTime date valide.
     * @throws ValidatorDateException lance une exception si erreur sur la date.
     */
    public static LocalDateTime validIntroduced(final LocalDateTime introduced) throws ValidatorDateException {
        if (introduced != null) {
            if (!DateValidation.validDateInBetween(introduced, BEGIN_DATE_VALID, END_DATE_VALID)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                throw new ValidatorDateException("Introduced date n'est pas comprise entre " + BEGIN_DATE_VALID.format(formatter) + " et " + END_DATE_VALID.format(formatter));
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
    public static LocalDateTime validDiscontinued(final LocalDateTime discontinued) throws ValidatorDateException {
        if (discontinued != null) {
            if (!DateValidation.validDateInBetween(discontinued, BEGIN_DATE_VALID, END_DATE_VALID)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                throw new ValidatorDateException("Discontinued date n'est pas comprise entre " + BEGIN_DATE_VALID.format(formatter) + " et " + END_DATE_VALID.format(formatter));
            }
        }
        return discontinued;
    }

}
