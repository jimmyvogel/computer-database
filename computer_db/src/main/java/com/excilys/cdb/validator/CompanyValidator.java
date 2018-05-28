package com.excilys.cdb.validator;

import com.excilys.cdb.validator.exceptions.ValidatorStringException;

/**
 * Validator for instance variables of a company.
 * @author vogel
 *
 */
public class CompanyValidator {

    public static final int TAILLE_MIN_NAME = 3;
    public static final int TAILLE_MAX_NAME = 60;

    /**
     * Validation of parameter name.
     * @param name string to validate
     * @return String the valid name
     * @throws ValidatorStringException exception sur le name.
     */
    public static String validName(final String name) throws ValidatorStringException {
        if (name == null) {
            throw new ValidatorStringException("Le nom est null");
        }
        if (name.length() < TAILLE_MIN_NAME) {
            throw new ValidatorStringException("Le nom est trop court " + TAILLE_MIN_NAME + " lettres minimum");
        }
        if (name.length() > TAILLE_MAX_NAME) {
            throw new ValidatorStringException("Le nom est trop long " + TAILLE_MAX_NAME + " lettres maximum");
        }
        return name;
    }

}
