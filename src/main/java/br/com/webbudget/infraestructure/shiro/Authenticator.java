/*
 * Copyright (C) 2018 Arthur Gregorio, AG.Software
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
package br.com.webbudget.infraestructure.shiro;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
        
/**
 *
 * @author Arthur Gregorio
 *
 * @since 3.0.0
 * @version 1.0.0, 31/01/2018
 */
@Named
@RequestScoped
public class Authenticator implements Serializable {
    
    private Subject subject;

    /**
     * 
     */
    @PostConstruct
    protected void initialize() {
        this.subject = SecurityUtils.getSubject();
    }
    
    /**
     * 
     * @param credential 
     */
    public void login(Credential credential) {
        this.subject.login(credential.asToken());
    }

    /**
     * 
     */
    public void logout() {
        this.subject.logout();
    }
    
    /**
     * 
     * @return 
     */
    public boolean authenticationIsNeeded() {
        return !this.subject.isAuthenticated();
    }
}
