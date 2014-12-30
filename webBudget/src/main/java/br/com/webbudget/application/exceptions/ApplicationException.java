package br.com.webbudget.application.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0
 * @since 1.0, 06/11/2013
 */
public final class ApplicationException extends AuthenticationException {

    /**
     * 
     * @param message 
     */
    public ApplicationException(String message) {
        super(message);
    }
    
    /**
     * 
     * @param message
     * @param throwable 
     */
    public ApplicationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
