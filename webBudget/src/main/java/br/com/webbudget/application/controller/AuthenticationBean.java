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

import br.com.webbudget.application.exceptions.ApplicationException;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.service.AccountService;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;

/**
 * Bean que controla a autenticacao no sistema, por ele invocamos o gerenciador
 * de autenticacao para que o usuario possa realizar acesso ao sistema
 *
 * @author Arthur Gregorio
 *
 * @version 1.1.1
 * @since 1.0.0, 06/10/2013
 */
@Named
@ViewScoped
public class AuthenticationBean extends AbstractBean {

    @Getter
    private User user;
    
    @Getter
    private boolean loginError;

    @Inject
    private transient AccountService accountService;

    /**
     * Inicializa a pagina, verificamos se ja nao existe alguem logado, se nao
     * existir, inicializa o usuario e boa. Se nao, manda para a dashboard
     * 
     * @return pagina para redirecionar
     */
    public String initialize() {
        
        // se ja tem gente logada, manda para a dashboard
//        if (AccountService.getCurrentAuthenticatedUser() != null) {
//            return "/main/dashboard.xhtml?faces-redirect=true";
//        }
        
        this.user = new User();
        
        return null;
    }

    /**
     * Realiza o login, se houver erro redireciona para a home novamente e <br/>
     * impede que prossiga
     *
     * @return a home autenticada ou a home de login caso acesso negado
     */
    public String doLogin() {

        try {
//            this.accountService.login(this.user);
            return "/main/dashboard.xhtml?faces-redirect=true";
        } catch (ApplicationException ex) {
            this.error(ex.getMessage(), true);
            this.loginError = true;
            return "";
        } 
    }
}
