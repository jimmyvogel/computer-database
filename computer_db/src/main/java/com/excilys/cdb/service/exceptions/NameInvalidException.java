package com.excilys.cdb.service.exceptions;

public class NameInvalidException extends ServiceException {

    /**
     */
    private static final long serialVersionUID = 6765269871660816698L;

    /**
     * Exception sur le nom d'un objet utilisé par le service.
     * @param name le nom de l'objet
     * @param reason la raison de l'erreur
     */
    public NameInvalidException(String name, String reason) {
        super("Le nom : " + name + " est invalid car : " + reason);
    }

}
