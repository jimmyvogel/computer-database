package com.excilys.cdb.service.exceptions;

public class DateInvalidException extends ServiceException {

    /**
     */
    private static final long serialVersionUID = 1889849919272711605L;

    /**
     * Exception sur la date d'un objet utilisé par le service.
     * @param reason la raison de l'erreur
     */
    public DateInvalidException(String reason) {
        super(reason);
    }

}
