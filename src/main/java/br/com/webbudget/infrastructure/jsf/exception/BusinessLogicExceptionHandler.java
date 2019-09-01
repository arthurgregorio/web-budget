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

import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.infrastructure.jsf.FacesUtils;
import br.com.webbudget.infrastructure.i18n.MessageSource;
import org.omnifaces.util.Exceptions;
import org.omnifaces.util.Messages;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

/**
 * The {@link BusinessLogicException} handler
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/11/2018
 */
public class BusinessLogicExceptionHandler implements CustomExceptionHandler<BusinessLogicException> {

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param exception
     */
    @Override
    public void handle(FacesContext context, BusinessLogicException exception) {

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(exception);
        }

        final String i18nMessage = MessageSource.get(exception.getMessage());

        FacesUtils.clearMessages(context);
        Messages.add(FacesMessage.SEVERITY_ERROR, null, i18nMessage, exception.getParameters());

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
        return Exceptions.is(throwable, BusinessLogicException.class);
    }
}
