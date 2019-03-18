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

import br.com.webbudget.domain.entities.configuration.Profile;
import br.com.webbudget.domain.entities.configuration.User;
import br.com.webbudget.domain.repositories.configuration.UserRepository;
import br.com.webbudget.infrastructure.cdi.qualifiers.AuthenticatedUser;
import lombok.Getter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * The controller of the user session. This class hold a session for the user and his authorization data
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
    @Getter
    private Profile profile;

    @Inject
    private UserRepository userRepository;

    /**
     * Initialize the session
     */
    @PostConstruct
    protected void initialize() {

        final String principalUsername = String.valueOf(this.getSubject().getPrincipal());

        this.principal = this.userRepository
                .findByUsername(principalUsername)
                .orElseThrow(() -> new AuthenticationException(String.format("User %s has no local user", principalUsername)));

        this.profile = this.principal.getProfile();
    }

    /**
     * @return if the current session of the user is valid or not
     */
    public boolean isValid() {
        final Subject subject = this.getSubject();
        return subject.isAuthenticated() && subject.getPrincipal() != null;
    }

    /**
     * To check if the given role is permitted to the current user
     *
     * @param role the role to be tested
     * @return true if is permitted, false otherwise
     */
    public boolean hasRole(String role) {
        return this.getSubject().hasRole(role);
    }

    /**
     * To check if the given permission is granted to the current user
     *
     * @param permission the permission to be tested
     * @return true if is granted, false otherwise
     */
    public boolean isPermitted(String permission) {
        return this.getSubject().isPermitted(permission);
    }

    /**
     * @return return the current {@link Subject} of the application
     */
    private Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * Simple producer to make the user object of the current principal available
     * to other functionalities of the system, like the audit mechanism
     *
     * @return the current principal user object
     */
    @Produces
    @AuthenticatedUser
    User producePrincipal() {
        return this.principal;
    }
}

