/*
 * Copyright (C) 2017 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.converter;

import br.com.webbudget.application.component.Color;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.1, 22/01/2017
 */
@FacesConverter("rgbColorConverter")
public class RgbColorConverter implements Converter { 

    /**
     * 
     * @param context
     * @param component
     * @param value
     * @return 
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return Color.parse(value);
    }

    /**
     * 
     * @param context
     * @param component
     * @param value
     * @return 
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Color) value).toString();
    }
}
