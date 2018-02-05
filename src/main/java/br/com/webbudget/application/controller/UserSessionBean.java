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

import br.com.webbudget.domain.entities.security.User;
import br.com.webbudget.domain.repositories.tools.UserRepository;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.0.0, 08/01/2018
 */
@Named
@SessionScoped
public class UserSessionBean implements Serializable {

    @Getter
    private User principal;

    @Inject
    private UserRepository userRepository;
    
    /**
     * 
     */
    @PostConstruct
    protected void initialize() {
        
        final String principalUsername = String.valueOf(
                this.getSubject().getPrincipal());
        
        this.principal = this.userRepository
                .findOptionalByUsername(principalUsername)
                .orElseThrow(() -> new AuthenticationException(String.format(
                        "User %s not found", principalUsername)));
    }
        
    /**
     * 
     * @return 
     */
    public boolean isValid() {
        final Subject subject = this.getSubject();
        return subject.isAuthenticated() && subject.getPrincipal() != null;
    }
    
    /**
     * 
     * @param role
     * @return 
     */
    public boolean hasRole(String role) {
        return this.getSubject().hasRole(role);
    }
    
    /**
     * 
     * @param permission
     * @return 
     */
    public boolean isPermitted(String permission) {
        return this.getSubject().isPermitted(permission);
    }

    /**
     * 
     * @return 
     */
    public String getMenuStyle() {
        return "skin-black";
    }
    
    /**
     * 
     * @return 
     */
    private Subject getSubject() {
        return SecurityUtils.getSubject();
    }
}

