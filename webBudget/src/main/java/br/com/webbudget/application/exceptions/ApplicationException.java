/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

    private Object[] parameters;
    
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
     * @param parameters 
     */
    public ApplicationException(String message, Object... parameters) {
        super(message);
        this.parameters = parameters;
    }
    
    /**
     * 
     * @param message
     * @param throwable
     * @param parameters 
     */
    public ApplicationException(String message, Throwable throwable, Object... parameters) {
        super(message, throwable);
        this.parameters = parameters;
    }
}
