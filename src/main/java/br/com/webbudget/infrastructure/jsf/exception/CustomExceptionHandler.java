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
import br.com.webbudget.infrastructure.utils.MessageSource;
import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.config.WebXml;
import org.omnifaces.util.Exceptions;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.RollbackException;
import java.util.Iterator;

import static javax.servlet.RequestDispatcher.*;

/**
 * The customized {@link ExceptionHandlerWrapper} to make the handling of
 * exceptions more easy for the managed beans
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/02/2018
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    /**
     * Constructor...
     *
     * @param exceptionHandler the wrapped handler
     */
    CustomExceptionHandler(ExceptionHandler exceptionHandler) {
        super(exceptionHandler);
    }

    /**
     * {@inheritDoc }
     *
     * @throws FacesException
     */
    @Override
    public void handle() throws FacesException {
        final FacesContext context = FacesContext.getCurrentInstance();
        handleException(context);
        this.getWrapped().handle();
    }

    /**
     * Method to handle the generic exception and take a decision of witch step to take after identify the type of the
     * exception.
     *
     * For {@link BusinessLogicException} or {@link ConstraintViolationException} display a simple message on the UI,
     * if unknown exception is given, call the error page configured in the web.xml
     *
     * @param context the faces context to use on the handling process
     */
    private void handleException(FacesContext context) {

        final Iterator<ExceptionQueuedEvent> unhandled = getUnhandledExceptionQueuedEvents().iterator();

        if (unhandled.hasNext()) {

            final Throwable throwable = unhandled.next()
                    .getContext()
                    .getException();

            unhandled.remove();

            final Throwable rootCause = Exceptions.unwrap(throwable);

            if (Exceptions.is(rootCause, BusinessLogicException.class)) {
                this.handleBusinessException(context, (BusinessLogicException) rootCause);
                return;
            } else if (Exceptions.is(rootCause, ConstraintViolationException.class)) {
                this.handleConstraintViolationException(context, (RollbackException) rootCause);
                return;
            }

            // if is not a model exception, go to the generic error page
            goToErrorPage(context, rootCause);
        }
    }

    /**
     * Send to the error page
     *
     * @param context the context
     * @param ex the exception to fill the details
     */
    private void goToErrorPage(FacesContext context, Throwable ex) {

        final HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();

        request.setAttribute(ERROR_EXCEPTION + "_stacktrace", ex);

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(ex);
        }

        request.setAttribute(ERROR_EXCEPTION_TYPE, ex.getClass().getName());
        request.setAttribute(ERROR_MESSAGE, ex.getMessage());
        request.setAttribute(ERROR_REQUEST_URI, request.getHeader("Referer"));
        request.setAttribute(ERROR_STATUS_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        final String errorPage = getErrorPage(ex);

        context.getApplication().getNavigationHandler()
                .handleNavigation(context, null, errorPage);

        context.renderResponse();
    }

    /**
     * Find in the web.xml the path to the error page
     *
     * @param exception the exception type to check
     * @return the page
     */
    private String getErrorPage(Throwable exception) {

        if (exception instanceof EJBException && exception.getCause() != null) {
            exception = exception.getCause();
        }
        return WebXml.instance().findErrorPageLocation(exception);
    }

    /**
     * Handle the {@link BusinessLogicException}
     *
     * @param context the context
     * @param ex the exception to fill the details
     */
    private void handleBusinessException(FacesContext context, BusinessLogicException ex) {

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(ex);
        }

        final String i18nMessage = MessageSource.get(ex.getMessage());

        this.clearMessages(context);
        Messages.add(FacesMessage.SEVERITY_ERROR, null, i18nMessage, ex.getParameters());

        context.renderResponse();

        this.temporizeHiding();
    }

    /**
     * Handle the {@link ConstraintViolationException}
     *
     * @param context the context
     * @param ex the exception to fill the details
     */
    private void handleConstraintViolationException(FacesContext context, RollbackException ex) {

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(ex);
        }

        this.clearMessages(context);
        Messages.add(FacesMessage.SEVERITY_ERROR, null, MessageSource.get("error.core.constraint-violation"));

        context.renderResponse();

        this.temporizeHiding();
    }

    /**
     * Clear the messages component on the user screen
     *
     * @param context the view context to be used
     */
    private void clearMessages(FacesContext context) {

        final Iterator<FacesMessage> messages = context.getMessages();

        while (messages.hasNext()) {
            messages.next();
            messages.remove();
        }
    }

    /**
     * After display the message, temporize the hiding of the message box
     */
    private void temporizeHiding() {
        PrimeFaces.current().executeScript("setTimeout(\"$(\'#messages\').slideUp(300)\", 8000)");
    }
}