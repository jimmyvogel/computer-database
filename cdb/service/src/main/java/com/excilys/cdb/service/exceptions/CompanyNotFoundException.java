package com.excilys.cdb.service.exceptions;

import java.util.Collections;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.servicemessage.ServiceMessage;

public class CompanyNotFoundException extends ServiceException {

    /**
     */
    private static final long serialVersionUID = -7956627033406728774L;

    /**
     * Exception throw when a company is not found in the bdd.
     * @param id the id specified.
     */
    public CompanyNotFoundException(long id) {
        super(MessageHandler.getMessage(ServiceMessage.COMPANY_NOT_FOUND, Collections.singleton(id).toArray()));
    }

}
