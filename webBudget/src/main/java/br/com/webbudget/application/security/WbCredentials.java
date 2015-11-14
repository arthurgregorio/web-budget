/*
 * Copyright (C) 2015 arthur
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
package br.com.webbudget.application.security;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.picketlink.idm.credential.AbstractBaseCredentials;
import org.picketlink.idm.credential.Password;

/**
 * Customizacao das credenciais de login para possibilitar a validacao do form
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.1.2, 14/11/2015
 */
@Named
@RequestScoped
public class WbCredentials extends AbstractBaseCredentials {

    @Getter
    @Setter
    @NotEmpty(message = "{authentication.username}")
    private String username;
    private Object secret;

    /**
     * 
     * @return 
     */
    @NotNull(message = "{authentication.password}")
    public String getPassword() {
        if (this.secret instanceof Password) {
            Password password = (Password) this.secret;
            return new String(password.getValue());
        }
        return null;
    }

    /**
     * 
     * @param password 
     */
    public void setPassword(final String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password can not be null.");
        }
        this.secret = new Password(password.toCharArray());
    }

    /**
     * 
     */
    @Override
    public void invalidate() {
        this.secret = null;
        this.username = null;
    }

    /**
     * 
     * @return 
     */
    @Override
    public String toString() {
        return "DefaultLoginCredentials[" + (this.username != null ? this.username : "unknown") + "]";
    }
}
