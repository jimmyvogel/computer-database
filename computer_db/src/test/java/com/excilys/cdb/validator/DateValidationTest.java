package com.excilys.cdb.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

public class DateValidationTest {

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateOk() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        Assert.assertNotNull(DateValidation.validationDate(formatter.format(now)));
        Assert.assertNotNull(DateValidation.validationDate(now.toString()));
    }

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateFailRegex() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-yyyy-dd");
        LocalDateTime now = LocalDateTime.now();
        Assert.assertNull(DateValidation.validationDate(formatter.format(now)));
    }

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateStringEmpty() {
        Assert.assertNull(DateValidation.validationDate(""));
    }

    /**
     * DateValidation.
     */
    @Test
    public void testValidationDateStringNull() {
        Assert.assertNull(DateValidation.validationDate(null));
    }

}
