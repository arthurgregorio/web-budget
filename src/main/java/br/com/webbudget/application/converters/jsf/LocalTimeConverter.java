/*
 * Copyright (C) 2015 Arthur Gregorio, AG.Software
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
package br.com.webbudget.application.converters.jsf;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Converter para manter a compatibilidade dos componentes hora do primefaces
 * com o java.time.LocalTime do Java 8
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 1.2.0, 27/08/2014
 */
@FacesConverter("localTimeConverter")
public class LocalTimeConverter implements Converter {

    /**
     * Manda o objeto para o bean convertido em LocalTime
     * 
     * @param context
     * @param component
     * @param value
     * 
     * @return 
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value != null ? LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm")) : null;
    }

    /**
     * Manda o objeto para a view, em String
     * 
     * @param context
     * @param component
     * @param value
     * 
     * @return 
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
        final LocalTime time = (LocalTime) value;
        
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
