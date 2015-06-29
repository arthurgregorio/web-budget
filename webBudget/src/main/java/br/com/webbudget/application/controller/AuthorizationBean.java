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

import br.com.webbudget.application.producer.qualifier.AuthenticatedUser;
import br.com.webbudget.domain.security.Authorization;
import br.com.webbudget.domain.security.Role;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.service.AccountService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import static org.picketlink.idm.model.basic.BasicModel.getRole;

/**
 * Bean utlizado pelo sistema para requisitar as authorities disponiveis no
 * sistemas
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 27/06/2015
 */
@Named
@ApplicationScoped
public class AuthorizationBean {

    @Getter
    @Inject
    private Authorization authorization;
    
    @Inject
    @AuthenticatedUser
    private User authenticatedUser;
    
    @Inject
    private AccountService accountService;

    /**
     * Checa pela role de um respectivo usuario
     * 
     * @param roleName a role que espera-se que este usuario tenha
     * @return se existe ou nao uma instancia desta role atribuida a ele
     */
    public boolean hasRole(String roleName) {
        
        boolean hasRole = this.accountService.userHasRole(this.authenticatedUser, 
                this.accountService.findRoleByName(roleName));
        
        return hasRole;
    }
    
    /**
     * @return o nome do usuario logado atualmente no sistema
     */
    public String getAuthenticatedUserName() {
        return this.authenticatedUser.getName();
    }
}
