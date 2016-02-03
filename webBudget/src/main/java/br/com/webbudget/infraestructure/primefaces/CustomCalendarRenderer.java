/*
 * Copyright (C) 2016 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infraestructure.primefaces;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.calendar.CalendarRenderer;
import org.primefaces.context.RequestContext;
import org.primefaces.util.HTML;

/**
 * Renderizador customizado para o calendar dentro da aplicacao
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.2.0, 03/02/2016
 */
public class CustomCalendarRenderer extends CalendarRenderer {

    private static final String CUSTOM_CLASSES = "form-control";

    /**
     * Novamente mesma treta do CustomInputNumberRenderer so que para o calendar
     * 
     * @param context
     * @param calendar
     * @param id
     * @param value
     * @param popup
     * 
     * @throws IOException
     */
    @Override
    protected void encodeInput(FacesContext context, Calendar calendar, 
            String id, String value, boolean popup) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        String type = popup ? "text" : "hidden";
        String labelledBy = calendar.getLabelledBy();

        writer.startElement("input", null);
        writer.writeAttribute("id", id, null);
        writer.writeAttribute("name", id, null);
        writer.writeAttribute("type", type, null);

        if (calendar.isRequired()) {
            writer.writeAttribute("aria-required", "true", null);
        }

        if (!isValueBlank(value)) {
            writer.writeAttribute("value", value, null);
        }

        if (popup) {
            String inputStyleClass = Calendar.INPUT_STYLE_CLASS + " " + CUSTOM_CLASSES;
            if (calendar.isDisabled()) {
                inputStyleClass = inputStyleClass + " ui-state-disabled";
            }
            if (!calendar.isValid()) {
                inputStyleClass = inputStyleClass + " ui-state-error";
            }

            writer.writeAttribute("class", inputStyleClass, null);

            if (calendar.isReadonly() || calendar.isReadonlyInput()) {
                writer.writeAttribute("readonly", "readonly", null);
            }
            if (calendar.isDisabled()) {
                writer.writeAttribute("disabled", "disabled", null);
            }

            renderPassThruAttributes(context, calendar, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
            renderDomEvents(context, calendar, HTML.INPUT_TEXT_EVENTS);
        }

        if (labelledBy != null) {
            writer.writeAttribute("aria-labelledby", labelledBy, null);
        }

        if (RequestContext.getCurrentInstance().getApplicationContext()
                .getConfig().isClientSideValidationEnabled()) {
            renderValidationMetadata(context, calendar);
        }

        writer.endElement("input");
    }
}
