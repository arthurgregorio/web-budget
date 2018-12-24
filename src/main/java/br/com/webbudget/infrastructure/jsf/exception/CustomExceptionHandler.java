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
package br.com.webbudget.infrastructure.jsf.exception;

import javax.faces.context.FacesContext;

/**
 * The contract to create new exception handlers to be used with the {@link CustomExceptionHandlerWrapper}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/11/2018
 */
public interface CustomExceptionHandler<T extends Throwable> {

    /**
     * This is the method responsible to handle the exception itself
     *
     * @param context current {@link FacesContext} to get access to the view
     * @param exception the exception to be handled
     */
    void handle(FacesContext context, T exception);

    /**
     * To check if the throwable passed as parameter can be handled by this handler
     *
     * @param throwable the throwable to test
     * @return true if is acceptable or  false if not
     */
    boolean accept(Throwable throwable);
}
