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

import br.com.webbudget.application.components.ui.AbstractBean;
import br.eti.arthurgregorio.shiroee.auth.Authenticator;
import br.eti.arthurgregorio.shiroee.auth.Credential;
import lombok.Getter;
import org.apache.shiro.authc.AuthenticationException;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Bean to control the authentication process
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 06/10/2013
 */
@Named
@ViewScoped
public class AuthenticationBean extends AbstractBean {
    
    @Getter
    private Credential credential;
    
    @Inject
    private Authenticator authenticator;
    
    /**
     * Initializer method
     *
     * @return if login is needed, return empty. If not return the outcome to the dashboard page
     */
    public String initialize() {
        if (this.authenticator.authenticationIsNeeded()) {
            this.credential = new Credential();
            return "";
        } else {
            return "/secured/dashboard.xhtml?faces-redirect=true";
        }
    }
    
    /**
     * Start the login process
     *
     * @return the login success page
     */
    public String doLogin() {
        try {
            this.authenticator.login(this.credential);
            return "/secured/dashboard.xhtml?faces-redirect=true";
        } catch (AuthenticationException ex) {
            this.addError(true, "error.authentication.failed");
            return null;
        }
    }
    
    /**
     * Start the logout process
     * 
     * @return the logout success page
     */
    public String doLogout() {
        this.authenticator.logout();
        return "/index.xhtml?faces-redirect=true";
    }
}

