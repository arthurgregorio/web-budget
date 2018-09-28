/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.exceptions;

import lombok.Getter;

/**
 * This class represents all the business exceptions of the application
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 17/01/2018
 */
public class BusinessLogicException extends RuntimeException {

    @Getter
    private Object[] parameters;

    /**
     * Constructor...
     *
     * @param message the message to describe the error
     */
    public BusinessLogicException(String message) {
        super(message);
    }

    /**
     * Constructor...
     *
     * @param message the message to describe the error
     * @param parameters the parameters to fill in the message
     */
    public BusinessLogicException(String message, Object... parameters) {
        super(message);
        this.parameters = parameters;
    }

    /**
     * Constructor...
     *
     * @param message the message to describe the error
     * @param throwable the instance of the exception to compose the stack
     * @param parameters the parameters to fill in the message
     */
    public BusinessLogicException(String message, Throwable throwable, Object... parameters) {
        super(message, throwable);
        this.parameters = parameters;
    }

    /**
     * Create a new instance of this exception
     *
     * @param message the message to be used
     * @return this exception
     */
    public static BusinessLogicException create(String message) {
        return new BusinessLogicException(message);
    }

    /**
     * Create a new instance of this exception
     *
     * @param message the message to be used
     * @param parameters the parameters to be passed to the message
     * @return this exception
     */
    public static BusinessLogicException create(String message, Object... parameters) {
        return new BusinessLogicException(message, parameters);
    }

    /**
     * Create a new instance of this exception
     *
     * @param message the message to be used
     * @param throwable the {@link Throwable} of this exception
     * @param parameters the parameters to be passed to the message
     * @return this exception
     */
    public static BusinessLogicException create(String message, Throwable throwable, Object... parameters) {
        return new BusinessLogicException(message, throwable, parameters);
    }
}