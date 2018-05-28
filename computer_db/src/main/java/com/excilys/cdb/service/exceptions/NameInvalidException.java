package com.excilys.cdb.service.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class NameInvalidException extends ServiceException {

	  @Autowired
	  private Environment env;
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
        System.out.println(env.getProperty("exception.computer.name.short"));
    }

}
