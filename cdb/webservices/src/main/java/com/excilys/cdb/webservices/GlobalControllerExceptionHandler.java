package com.excilys.cdb.webservices;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.excilys.cdb.messagehandler.MessageHandler;
import com.excilys.cdb.webservices.exceptions.IllegalSearchException;
import com.excilys.cdb.webservices.exceptions.WebServiceException;
import com.excilys.cdb.webservices.message.WebServiceMessage;

@RestControllerAdvice
class GlobalControllerExceptionHandler {
	
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleConflict() {
    	String mess = MessageHandler.getMessage(WebServiceMessage.CONFLIT_EXCEPTION, null);
		return new ResponseEntity<String>(mess, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleServiceException(IllegalArgumentException ex) {
    	String mess = MessageHandler.getMessage(WebServiceMessage.TYPE_MISMATCH, null);
		return new ResponseEntity<String>(mess, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IllegalSearchException.class)
    public ResponseEntity<String> handleSearchException(IllegalSearchException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(WebServiceException.class)
    public ResponseEntity<String> handleServiceException(WebServiceException se) {
		return new ResponseEntity<String>(se.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
//    @ExceptionHandler({ Exception.class })
//    public ResponseEntity<Object> handleAll(Exception ex) {
//    	String mess = MessageHandler.getMessage(WebServiceMessage.UNCHECK_EXCEPTION, null);
//        return new ResponseEntity<Object>(mess, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}