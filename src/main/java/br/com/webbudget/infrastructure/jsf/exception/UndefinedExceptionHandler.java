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

import org.omnifaces.config.WebXml;

import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.RequestDispatcher.*;

/**
 * Generic handler for any type of exception, by default the {@link CustomExceptionHandlerWrapper} use this
 * implementation to handle exceptions not mapped with a specific {@link CustomExceptionHandler}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/11/2018
 */
public class UndefinedExceptionHandler implements CustomExceptionHandler<Throwable> {

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param exception
     */
    @Override
    public void handle(FacesContext context, Throwable exception) {

        final HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        request.setAttribute(ERROR_EXCEPTION + "_stacktrace", exception);

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(exception);
        }

        request.setAttribute(ERROR_EXCEPTION_TYPE, exception.getClass().getName());
        request.setAttribute(ERROR_MESSAGE, exception.getMessage());
        request.setAttribute(ERROR_REQUEST_URI, request.getHeader("Referer"));
        request.setAttribute(ERROR_STATUS_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        final String errorPage = this.getErrorPage(exception);

        context.getApplication().getNavigationHandler().handleNavigation(context, null, errorPage);

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
        return true; // any exception
    }

    /**
     * Call the {@link WebXml} instance to get the generic error page path
     *
     * @param exception the exception to check for specific pages
     * @return the error page
     */
    private String getErrorPage(Throwable exception) {
        if (exception instanceof EJBException && exception.getCause() != null) {
            exception = exception.getCause();
        }
        return WebXml.instance().findErrorPageLocation(exception);
    }
}
