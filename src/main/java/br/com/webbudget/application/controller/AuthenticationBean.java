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

import br.com.webbudget.infraestructure.shiro.Authenticator;
import br.com.webbudget.infraestructure.shiro.Credential;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Bean que controla a autenticacao no sistema, por ele invocamos o gerenciador
 * de autenticacao para que o usuario possa realizar acesso ao sistema
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 1.0.0, 06/10/2013
 */
@Named
@ViewScoped
public class AuthenticationBean extends AbstractBean {

    @Inject
    private Authenticator authenticator;
    
    @Getter
    private Credential credential;
    
    /**
     * 
     * @return 
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
     *
     * @return
     */
    public String doLogin() {
        try {
            this.authenticator.login(this.credential);
            return "/secured/dashboard.xhtml?faces-redirect=true";
        } catch (AuthenticationException ex) {
            this.logger.error("Login error", ex);
            this.addError(true, "error.authentication-failed");
            return null;
        } catch (Exception ex) {
            this.logger.error("Login error", ex);
            this.addError(true, "error.generic-error", ex);
            return null;
        }
    }
    
    /**
     * 
     * @return 
     */
    public String doLogout() {
        this.authenticator.logout();
        return "/index.xhtml?faces-redirect=true";
    }

}

