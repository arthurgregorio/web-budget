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
package br.com.webbudget.application.controller;

import br.com.webbudget.domain.model.entity.tools.Configuration;
import br.com.webbudget.domain.model.service.ConfigurationService;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.picketlink.authentication.event.LoggedInEvent;
import org.picketlink.authentication.event.PostLoggedOutEvent;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.0, 07/09/2015
 */
@Named
@SessionScoped
public class AppConfigBean implements Serializable {

    @Getter
    private Configuration configuration;
    
    @Inject
    private transient ConfigurationService configurationService;
    
    /**
     * 
     * @param event 
     */
    protected void initialize(@Observes LoggedInEvent event) {
        this.configuration = this.configurationService.loadDefault();
    }
    
    /**
     * 
     * @param event 
     */
    protected void destroy(@Observes PostLoggedOutEvent event) {
        this.configuration = null;
    }
}
