package com.excilys.cdb.service.exceptions;

import java.util.Collections;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.servicemessage.ServiceMessage;

public class ComputerNotFoundException extends ServiceException {

    /**
     */
    private static final long serialVersionUID = -8927718135567241161L;

    /**
     * Exception throw when a computer is not found in the bdd.
     * @param id the id specified.
     */
    public ComputerNotFoundException(final long id) {
        super(MessageHandler.getMessage(ServiceMessage.COMPUTER_NOT_FOUND, Collections.singleton(id).toArray()));
    }

}
