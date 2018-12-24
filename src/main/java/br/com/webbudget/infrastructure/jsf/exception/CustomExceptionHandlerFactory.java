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

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import java.util.Set;

/**
 * Simple {@link ExceptionHandlerFactory} to customize the exception handling by JSF
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/02/2018
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {

    /**
     * {@inheritDoc}
     *
     * @param wrapped
     */
    public CustomExceptionHandlerFactory(ExceptionHandlerFactory wrapped) {
        super(wrapped);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public ExceptionHandler getExceptionHandler() {

        final Set<CustomExceptionHandler> handlers = Set.of(
                new BusinessLogicExceptionHandler(),
                new ConstraintViolationExceptionHandler()
        );

        return new CustomExceptionHandlerWrapper(this.getWrapped().getExceptionHandler(), handlers);
    }
}
