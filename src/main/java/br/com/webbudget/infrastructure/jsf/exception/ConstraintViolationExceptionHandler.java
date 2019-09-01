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

import br.com.webbudget.infrastructure.jsf.FacesUtils;
import br.com.webbudget.infrastructure.i18n.MessageSource;
import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.util.Exceptions;
import org.omnifaces.util.Messages;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

/**
 * The {@link ConstraintViolationException} handler
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/11/2018
 */
public class ConstraintViolationExceptionHandler implements CustomExceptionHandler {

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param exception
     */
    @Override
    public void handle(FacesContext context, Throwable exception) {

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(exception);
        }

        FacesUtils.clearMessages(context);
        Messages.add(FacesMessage.SEVERITY_ERROR, null, MessageSource.get("error.core.constraint-violation"));

        context.renderResponse();
    }

    /**
     * {@inheritDoc}
     *
     * @param throwable
     * @return
     */
    @Override
    public boolean accept(Throwable throwable) {
        return Exceptions.is(throwable, ConstraintViolationException.class);
    }
}