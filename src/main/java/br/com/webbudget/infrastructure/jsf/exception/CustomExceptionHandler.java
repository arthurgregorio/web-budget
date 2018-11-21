package br.com.webbudget.infrastructure.jsf.exception;

import javax.faces.context.FacesContext;

/**
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 20/11/2018
 */
public interface CustomExceptionHandler<T extends Throwable> {

    /**
     *
     * @param exception
     */
    void handle(FacesContext context, T exception);

    /**
     *
     * @param throwable
     * @return
     */
    boolean accept(Throwable throwable);
}
