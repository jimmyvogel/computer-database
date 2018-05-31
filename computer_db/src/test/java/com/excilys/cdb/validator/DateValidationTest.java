package com.excilys.cdb.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

import com.excilys.cdb.ressources.DefaultValues;

public class DateValidationTest {

	private static final String DATE_VALID = "2000-12-12";

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateStringOk() {
        Assert.assertNotNull(DateValidation.validDateFormat(DATE_VALID));
    }

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateOk() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DefaultValues.PATTERN_DATE);
        LocalDateTime now = LocalDateTime.now();
        Assert.assertNotNull(DateValidation.validDateFormat(formatter.format(now)));
    }

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateStringEmpty() {
        Assert.assertNull(DateValidation.validDateFormat(""));
    }

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateStringNull() {
        Assert.assertNull(DateValidation.validDateFormat(null));
    }

}
