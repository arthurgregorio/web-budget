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
package br.com.webbudget.application.controller;

import br.com.webbudget.infrastructure.utils.Configurations;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilities controller to provide common functionality to all the pages
 * 
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.3.0, 25/09/2016
 */
@Named
@RequestScoped
public class UtilsBean {
    
    @Inject
    private FacesContext facesContext;
    
    /**
     * Get the application base URL
     *
     * @return application base URL
     */
    public String getBaseURL() {
        
        final HttpServletRequest request = (HttpServletRequest) this.facesContext.getExternalContext().getRequest();
        
        return request.getRequestURL()
                .toString()
                .replace(request.getRequestURI(), "/");
    }
    
    /**
     * Get the actual date as {@link String}
     *
     * @return actual date in {@link String} formatted in dd/MM/yyyy
     */
    public String getActualDate() {
        return DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .format(LocalDate.now());
    }

    /**
     * Get the actual date and time as {@link String}
     *
     * @return actual date and time as {@link String} formatted in dd/MM/yyyy HH:mm
     */
    public String getActualDateTime() {
        return DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm")
                .format(LocalDateTime.now());
    }

    /**
     * Get from configurations the current version of the application
     *
     * @return application version
     */
    public String getApplicationVersion() {
        return Configurations.get("application.version");
    }

    /**
     * Get the current year to use at the little notice at the application footer
     *
     * @return current year for the copyright text
     */
    public String getCurrentYear() {
        return DateTimeFormatter.ofPattern("yyyy").format(LocalDate.now());
    }
}
