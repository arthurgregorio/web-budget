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

import br.com.webbudget.domain.security.Authorization;
import br.com.webbudget.domain.security.Group;
import br.com.webbudget.domain.security.User;
import br.com.webbudget.domain.service.AccountService;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.picketlink.Identity;
import org.picketlink.authentication.event.LoggedInEvent;
import org.picketlink.authentication.event.PostLoggedOutEvent;

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

    private List<Group> userGroups;
    
    @Getter
    @Inject
    private Authorization authorization;
    
    @Inject
    private Identity identity;
    
    @Inject
    private AccountService accountService;

    /**
     * 
     * @param event 
     */
    protected void initialize(@Observes LoggedInEvent event) {
        this.userGroups = new ArrayList<>();
    }
    
    /**
     * 
     * @param event 
     */
    protected void destroy(@Observes PostLoggedOutEvent event) {
        this.userGroups = null;
    }
    
    /**
     * Checa pela role de um respectivo usuario
     * 
     * @param roleName a role que espera-se que este usuario tenha
     * @return se existe ou nao uma instancia desta role atribuida a ele
     */
    public boolean hasRole(String roleName) {
        
        if (this.userGroups.isEmpty()) {
            this.userGroups = this.accountService
                    .listUserGroupsAndGrants(this.getAuthenticatedUser());
        }
        
        return this.checkForGrantTo(roleName);
    }
    
    /**
     * 
     * @param role 
     */
    private boolean checkForGrantTo(String role) {
        
        for (Group group : this.userGroups) {
            
            // se o group nao contiver grants, falha
            if (!group.getGrants().isEmpty()) {
                
                if (group.getGrants().stream().anyMatch((grant) ->
                        (grant.getRole().getAuthorization().equals(role)))) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * @return o nome do usuario logado atualmente no sistema
     */
    public String getAuthenticatedUserName() {
        return this.getAuthenticatedUser().getName();
    }
    
    /**
     * @return o usuario autenticado
     */
    private User getAuthenticatedUser() {
        return (User) this.identity.getAccount();
    }
}