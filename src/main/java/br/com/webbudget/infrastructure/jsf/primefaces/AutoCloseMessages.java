/*
 * Copyright (C) 2019 Arthur Gregorio, AG.Software
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
 * @since 3.0.0, 07/01/2019
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
        writer.writeText("setTimeout(\"$(\'#" + this.sanitizeId(component.getClientId())
                + "\').slideUp(500)\", 8000)", null);
        writer.endElement("script");
        writer.append('\r');
        writer.append('\n');
    }

    /**
     * Sanitize the component to enable jQuery to find them
     *
     * @param actualId auto generated component id
     * @return the new sanitized id
     */
    private String sanitizeId(String actualId) {
        return actualId.replace(":", "\\\\\\\\:");
    }
}
