package br.com.webbudget.infrastructure.jsf.exception;

import br.com.webbudget.infrastructure.jsf.FacesUtils;
import br.com.webbudget.infrastructure.utils.MessageSource;
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
        FacesUtils.temporizeHiding("messages");
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