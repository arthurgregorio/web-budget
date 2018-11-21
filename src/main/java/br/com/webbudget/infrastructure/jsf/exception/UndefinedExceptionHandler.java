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
 * Generic handler to all unmapped exceptions
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
     * Find on the web.xml a path to the error page
     *
     * @param exception the exception type to check
     * @return the error page
     */
    private String getErrorPage(Throwable exception) {
        if (exception instanceof EJBException && exception.getCause() != null) {
            exception = exception.getCause();
        }
        return WebXml.instance().findErrorPageLocation(exception);
    }
}
