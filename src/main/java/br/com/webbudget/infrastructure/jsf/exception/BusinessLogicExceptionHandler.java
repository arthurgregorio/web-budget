package br.com.webbudget.infrastructure.jsf.exception;

import br.com.webbudget.domain.exceptions.BusinessLogicException;
import br.com.webbudget.infrastructure.jsf.FacesUtils;
import br.com.webbudget.infrastructure.utils.MessageSource;
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
        return Exceptions.is(throwable, BusinessLogicException.class);
    }
}
