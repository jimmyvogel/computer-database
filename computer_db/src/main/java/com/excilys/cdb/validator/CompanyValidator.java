package com.excilys.cdb.validator;

import java.util.Arrays;
import java.util.Collections;

import com.excilys.cdb.exception.ExceptionHandler;
import com.excilys.cdb.exception.ExceptionHandler.MessageException;
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
            throw new ValidatorStringException(
            		ExceptionHandler.getMessage(MessageException.VALIDATION_NAME_NULL, Collections.singleton(name).toArray()));
        }
        if (name.length() < TAILLE_MIN_NAME) {
            throw new ValidatorStringException(
            		ExceptionHandler.getMessage(
            				MessageException.VALIDATION_NAME_SHORT, Arrays.asList(name, TAILLE_MIN_NAME).toArray()));
        }
        if (name.length() > TAILLE_MAX_NAME) {
            throw new ValidatorStringException(ExceptionHandler.getMessage(
    				MessageException.VALIDATION_NAME_LONG, Arrays.asList(name, TAILLE_MAX_NAME).toArray()));
        }
        return name;
    }

}
