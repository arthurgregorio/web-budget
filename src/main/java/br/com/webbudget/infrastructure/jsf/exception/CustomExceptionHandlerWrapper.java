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

import org.omnifaces.util.Exceptions;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import java.util.Iterator;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * The customized {@link ExceptionHandlerWrapper} to make handling of exceptions more easy for the managed beans
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/02/2018
 */
public class CustomExceptionHandlerWrapper extends ExceptionHandlerWrapper {

    private final Set<CustomExceptionHandler> customExceptionHandlers;

    private final UndefinedExceptionHandler undefinedExceptionHandler;

    /**
     * Constructor...
     *
     * @param exceptionHandler the wrapped handler
     */
    CustomExceptionHandlerWrapper(ExceptionHandler exceptionHandler, Set<CustomExceptionHandler> customHandlers) {
        super(exceptionHandler);
        this.customExceptionHandlers = requireNonNull(customHandlers);
        this.undefinedExceptionHandler = new UndefinedExceptionHandler();
    }

    /**
     * {@inheritDoc }
     *
     * @throws FacesException
     */
    @Override
    public void handle() throws FacesException {
        handleException();
        this.getWrapped().handle();
    }

    /**
     * Method to handle the generic exception and take a decision of witch step to take after identify the type of the
     * exception
     *
     * All non mapped exceptions on the {@link #customExceptionHandlers} will treated as generic exceptions on the
     * {@link UndefinedExceptionHandler}
     */
    @SuppressWarnings("unchecked")
    private void handleException() {

        final Iterator<ExceptionQueuedEvent> unhandled = getUnhandledExceptionQueuedEvents().iterator();

        if (unhandled.hasNext()) {

            final Throwable throwable = unhandled.next()
                    .getContext()
                    .getException();

            unhandled.remove();

            final FacesContext context = FacesContext.getCurrentInstance();
            final Throwable rootCause = Exceptions.unwrap(throwable);

            this.customExceptionHandlers.stream()
                    .filter(handler -> handler.accept(rootCause))
                    .findFirst()
                    .ifPresentOrElse(handler -> handler.handle(context, rootCause),
                            () -> this.undefinedExceptionHandler.handle(context, rootCause));
        }
    }
}