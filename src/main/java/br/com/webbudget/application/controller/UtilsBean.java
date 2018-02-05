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

import br.com.webbudget.infraestructure.utils.Configurations;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
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
     * @return a base URL da aplicacao
     */
    public String getBaseURL() {
        
        final HttpServletRequest request = (HttpServletRequest) 
                this.facesContext.getExternalContext().getRequest();
        
        return request.getRequestURL()
                .toString()
                .replace(request.getRequestURI(), "/");
    }
    
    /**
     * @return a data atual formatada em string
     */
    public String getActualDate() {
        return DateTimeFormatter
                .ofPattern("dd/MM/yyyy")
                .format(LocalDate.now());
    }
    
    /**
     * @return a data e hora atual em formato string
     */
    public String getActualDateTime() {
        return DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm")
                .format(LocalDateTime.now());
    }
    
    /**
     * @return o canal para push das notificacoes de mensagens
     */
    public String getMessagesChannel() {
        
        final StringBuilder builder = new StringBuilder();
        
        final String baseURL = this.getBaseURL();
        
        if (baseURL.contains("https")) {
            builder.append(baseURL.replace("https", "wss"));
        } else {
            builder.append(baseURL.replace("http", "ws"));
        }
        
        builder.append("channels/messages");
        
        return builder.toString();
    }

    /**
     * 
     * @return 
     */
    public String getCurrentVersion() {
        return Configurations.get("application.version");
    }
    
    /**
     * 
     * @return 
     */
    public int getCurrentYear() {
        return LocalDate.now().getYear();
    }
}
