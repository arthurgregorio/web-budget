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

import br.com.webbudget.infraestructure.ApplicationUtils;
import javax.faces.application.ProjectStage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

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
    private Identity identity;
    
    @Getter
    private boolean authenticationError;

    /**
     * Iniciliazacao da pagina da login onde checamos pela existencia de uma 
     * sessao valida
     * 
     * @return a dashboard do sistema
     */
    public String initialize() {
        
        // validamos se nao existe uma sessao ativa
        if (this.identity.isLoggedIn()) {
            return "/main/dashboard.xhtml?faces-redirect=true";
        } 

        // permanecemos na pagina
        return null;
    }

    /**
     * Realiza o login, se houver erro redireciona para a home novamente e 
     * impede que prossiga
     *
     * @return a home autenticada ou a home de login caso acesso negado
     */
    public String doLogin() {
        
        final AuthenticationResult result = this.identity.login();
        
        if (result == AuthenticationResult.SUCCESS) {
            return "/main/dashboard.xhtml?faces-redirect=true";
        } 
        return null;
    }
    
    /**
     * Realiza logout do sistema
     * 
     * @return a home para login
     */
    public String doLogout() {
        this.identity.logout();
        return "/index.xhtml?faces-redirect=true";
    }
    
    /**
     * @return verifica se a aplicacao esta em teste ou se estamos em outro 
     * ambiente
     */
    public boolean isSystemTest() {
        return ApplicationUtils.isStageRunning(ProjectStage.SystemTest);
    }
}
