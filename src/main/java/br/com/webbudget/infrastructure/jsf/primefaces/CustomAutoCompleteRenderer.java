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

import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.autocomplete.AutoCompleteRenderer;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.*;

/**
 * Customization for bootstrap 3 compatibility in the {@link AutoComplete} component
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 3.0.0, 11/04/2019
 */
public class CustomAutoCompleteRenderer extends AutoCompleteRenderer {

    /**
     * {@inheritDoc}
     *
     * Important!
     *
     * This is a workaround to make the {@link AutoComplete} component appears correctly at the UI, the only difference
     * of this this method and the one declared at the original renderer {@link #encodeMultipleMarkup(FacesContext, AutoComplete)
     * is the line 81 that have a extra class in the end (for bootstrap): form-control
     *
     * @param context
     * @param ac
     * @throws IOException
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void encodeMultipleMarkup(FacesContext context, AutoComplete ac) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        String clientId = ac.getClientId(context);
        String inputId = clientId + "_input";
        List values;
        if (ac.isValid()) {
            values = (List) ac.getValue();
        } else {
            Object submittedValue = ac.getSubmittedValue();

            try {
                values = (List) this.getConvertedValue(context, ac, submittedValue);
            } catch (ConverterException var25) {
                values = Arrays.asList((String[]) ((String[]) submittedValue));
            }
        }

        List<String> stringValues = new ArrayList<>();
        boolean disabled = ac.isDisabled();
        String title = ac.getTitle();
        String style = ac.getStyle();
        String styleClass = ac.getStyleClass();
        styleClass = styleClass == null ? "ui-autocomplete ui-autocomplete-multiple" : "ui-autocomplete ui-autocomplete-multiple " + styleClass;
        String listClass = ac.isDropdown() ? "ui-autocomplete-multiple-container ui-autocomplete-dd-multiple-container ui-widget ui-inputfield ui-state-default ui-corner-left" : "ui-autocomplete-multiple-container ui-widget ui-inputfield ui-state-default ui-corner-all form-control";
        listClass = disabled ? listClass + " ui-state-disabled" : listClass;
        listClass = ac.isValid() ? listClass : listClass + " ui-state-error";
        String autocompleteProp = ac.getAutocomplete() != null ? ac.getAutocomplete() : "off";
        writer.startElement("div", (UIComponent) null);
        writer.writeAttribute("id", clientId, (String) null);
        writer.writeAttribute("class", styleClass, (String) null);
        if (style != null) {
            writer.writeAttribute("style", style, (String) null);
        }

        if (title != null) {
            writer.writeAttribute("title", title, (String) null);
        }

        writer.startElement("ul", (UIComponent) null);
        writer.writeAttribute("class", listClass, (String) null);
        if (values != null && !values.isEmpty()) {
            Converter converter = ComponentUtils.getConverter(context, ac);
            String var = ac.getVar();
            boolean pojo = var != null;
            Collection<Object> items = ac.isUnique() ? new HashSet(values) : values;
            Iterator var18 = ((Collection) items).iterator();

            while (var18.hasNext()) {
                Object value = var18.next();
                Object itemValue = null;
                String itemLabel = null;
                if (pojo) {
                    context.getExternalContext().getRequestMap().put(var, value);
                    itemValue = ac.getItemValue();
                    itemLabel = ac.getItemLabel();
                } else {
                    itemValue = value;
                    itemLabel = String.valueOf(value);
                }

                String tokenValue = converter != null ? converter.getAsString(context, ac, itemValue) : String.valueOf(itemValue);
                String itemStyleClass = "ui-autocomplete-token ui-state-active ui-corner-all";
                if (ac.getItemStyleClass() != null) {
                    itemStyleClass = itemStyleClass + " " + ac.getItemStyleClass();
                }

                writer.startElement("li", (UIComponent) null);
                writer.writeAttribute("data-token-value", tokenValue, (String) null);
                writer.writeAttribute("class", itemStyleClass, (String) null);
                String labelClass = disabled ? "ui-autocomplete-token-label-disabled" : "ui-autocomplete-token-label";
                writer.startElement("span", (UIComponent) null);
                writer.writeAttribute("class", labelClass, (String) null);
                writer.writeText(itemLabel, (String) null);
                writer.endElement("span");
                if (!disabled) {
                    writer.startElement("span", (UIComponent) null);
                    writer.writeAttribute("class", "ui-autocomplete-token-icon ui-icon ui-icon-close", (String) null);
                    writer.endElement("span");
                }

                writer.endElement("li");
                stringValues.add(tokenValue);
            }
        }

        writer.startElement("li", (UIComponent) null);
        writer.writeAttribute("class", "ui-autocomplete-input-token", (String) null);
        writer.startElement("input", (UIComponent) null);
        writer.writeAttribute("type", "text", (String) null);
        writer.writeAttribute("id", inputId, (String) null);
        writer.writeAttribute("name", inputId, (String) null);
        writer.writeAttribute("autocomplete", autocompleteProp, (String) null);
        this.renderAccessibilityAttributes(context, ac);
        this.renderPassThruAttributes(context, ac, HTML.INPUT_TEXT_ATTRS_WITHOUT_EVENTS);
        this.renderDomEvents(context, ac, HTML.INPUT_TEXT_EVENTS);
        writer.endElement("input");
        writer.endElement("li");
        writer.endElement("ul");
        if (ac.isDropdown()) {
            this.encodeDropDown(context, ac);
        }

        if (!ac.isDynamic()) {
            this.encodePanel(context, ac);
        }

        this.encodeHiddenSelect(context, ac, clientId, stringValues);
        writer.endElement("div");
    }
}
