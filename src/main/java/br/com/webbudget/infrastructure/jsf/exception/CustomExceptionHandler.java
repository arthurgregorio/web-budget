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
import java.util.Iterator;
import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PhaseId;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION_TYPE;
import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;
import static javax.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.RollbackException;
import org.hibernate.exception.ConstraintViolationException;
import org.omnifaces.config.WebXml;
import org.omnifaces.util.Exceptions;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 28/02/2018
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private final ExceptionHandler wrapped;

    /**
     *
     * @param exceptionHandler
     */
    public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
        this.wrapped = exceptionHandler;
    }

    /**
     *
     * @return
     */
    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    /**
     *
     * @throws FacesException
     */
    @Override
    public void handle() throws FacesException {
        final FacesContext context = FacesContext.getCurrentInstance();
        handleException(context);
        this.wrapped.handle();
    }

    /**
     *
     * @param context
     */
    private void handleException(FacesContext context) {

        final Iterator<ExceptionQueuedEvent> unhandled
                = getUnhandledExceptionQueuedEvents().iterator();

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

            // direct to the error page if the exceptions is not in the 
            // application model 
            goToErrorPage(context, rootCause);
        }
    }

    /**
     *
     * @param context
     * @param ex
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

        final String errorPage = getErroPage(ex);

        context.getApplication().getNavigationHandler()
                .handleNavigation(context, null, errorPage);
        
        context.renderResponse();
    }

    /**
     *
     * @param exception
     * @return
     */
    private String getErroPage(Throwable exception) {

        if (exception instanceof EJBException && exception.getCause() != null) {
            exception = exception.getCause();
        }
        return WebXml.INSTANCE.findErrorPageLocation(exception);
    }

    /**
     *
     * @param context
     * @param ex
     */
    private void handleBusinessException(FacesContext context, BusinessLogicException ex) {

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(ex);
        }

        final String i18nMessage = MessageSource.get(ex.getMessage());
        
        Messages.add(FacesMessage.SEVERITY_ERROR, null, 
                i18nMessage, ex.getParameters());

        context.renderResponse();
        
        this.temporizeHiding();
    }
    
    /**
     * 
     * @param context
     * @param ex 
     */
    private void handleConstraintViolationException(FacesContext context, RollbackException ex) {

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(ex);
        }

        Messages.add(FacesMessage.SEVERITY_ERROR, null, 
                MessageSource.get("error.core.constraint-violation"));

        context.renderResponse();
        
        this.temporizeHiding();
    }
    
    /**
     * After display the message, hide the message box
     */
    private void temporizeHiding() {
        PrimeFaces.current().executeScript("setTimeout(\"$(\'#messages\').slideUp(300)\", 8000)");
    }
}
