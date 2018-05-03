package com.excilys.cdb.validator.exceptions;

import java.time.LocalDateTime;

public class ValidatorDateException extends Exception {

    /**
     */
    private static final long serialVersionUID = -9132599808149416148L;
    private String reason;

    /**
     * Exception de type date dans la validation.
     * @param date la date non valid.
     * @param reason the reason of the exception.
     */
    public ValidatorDateException(LocalDateTime date, String reason) {
        super(date + " not valid : " + reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
