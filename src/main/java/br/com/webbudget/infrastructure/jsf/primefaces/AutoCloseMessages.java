package br.com.webbudget.infrastructure.jsf.primefaces;

import org.primefaces.component.messages.MessagesRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Customization to make the Messages component automatic close after some seconds
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.0.0, 07/01/2019
 */
public class AutoCloseMessages extends MessagesRenderer {

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param component
     * @throws IOException
     */
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {

        super.encodeEnd(context, component);

        final ResponseWriter writer = context.getResponseWriter();

        writer.write('\n');
        writer.startElement("script", null);
        writer.writeText("setTimeout(\"$(\'#" + component.getClientId() + "\').slideUp(300)\", 8000)", null);
        writer.endElement("script");
        writer.append('\r');
        writer.append('\n');
    }
}
