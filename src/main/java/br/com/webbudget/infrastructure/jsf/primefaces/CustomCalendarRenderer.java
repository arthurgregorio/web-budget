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
package br.com.webbudget.infrastructure.jsf.primefaces;

import org.primefaces.component.api.UICalendar;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.calendar.CalendarRenderer;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Customization for bootstrap 3 compatibility in the {@link Calendar} component
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.0
 * @since 2.2.0, 03/02/2016
 */
public class CustomCalendarRenderer extends CalendarRenderer {

    /**
     * {@inheritDoc}
     *
     * @param context
     * @param calendar
     * @param id
     * @param value
     * @param popup
     * @throws IOException
     */
    @Override
    protected void encodeInput(FacesContext context, UICalendar calendar, String id, String value, boolean popup) throws IOException {

        final String styleClass = calendar.getInputStyleClass() + " form-control";

        calendar.setInputStyleClass(styleClass);

        super.encodeInput(context, calendar, id, value, popup);
    }
}
