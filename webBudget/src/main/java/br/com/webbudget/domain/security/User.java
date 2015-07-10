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
package br.com.webbudget.domain.security;

import javax.persistence.Column;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.picketlink.idm.model.AbstractIdentityType;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.annotation.IdentityStereotype;
import org.picketlink.idm.query.QueryParameter;
import static org.picketlink.idm.model.annotation.IdentityStereotype.Stereotype.USER;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.IDENTITY_USER_NAME;
import org.picketlink.idm.model.annotation.Unique;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@IdentityStereotype(USER)
public class User extends AbstractIdentityType implements Account {

    @Getter
    @Setter
    @NotEmpty(message = "{user.name}")
    @Column(name = "name", length = 90, nullable = false)
    private String name;
    @Getter
    @Setter
    @Email(message = "{user.email}")
    @NotEmpty(message = "{user.email}")
    @Column(name = "email", length = 90, nullable = false)
    private String email;
    @Setter
    @Getter
    @Unique
    @StereotypeProperty(IDENTITY_USER_NAME)
    @NotEmpty(message = "{user.username}")
    @Column(name = "username", length = 45, nullable = false)
    private String username;

    @Getter
    @Setter
    @Transient
    private GroupMembership groupMembership;
    
    @Getter
    @Setter
    @Transient
    private boolean selected;
    @Getter
    @Setter
    @Transient
    private String password;
    @Getter
    @Setter
    @Transient
    private String passwordConfirmation;

    public static final QueryParameter NAME = QUERY_ATTRIBUTE.byName("name");
    public static final QueryParameter EMAIL = QUERY_ATTRIBUTE.byName("email");
    public static final QueryParameter USER_NAME = QUERY_ATTRIBUTE.byName("username");

    /**
     *
     */
    public User() {
        this(null);
    }

    /**
     *
     * @param username 
     */
    public User(String username) {
        this.username = username;
        this.groupMembership = new GroupMembership(null, this);
    }
    
    /**
     * 
     * @return 
     */
    public boolean isBlocked() {
        return !this.isEnabled();
    }
    
    /**
     * 
     * @param blocked 
     */
    public void setBlocked(boolean blocked) {
        this.setEnabled(!blocked);
    }
}
