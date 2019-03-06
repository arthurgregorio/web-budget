/*
 * Copyright (C) 2013 Arthur Gregorio, AG.Software
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
package br.com.webbudget.domain.entities.configuration;

import br.com.webbudget.domain.entities.PersistentEntity;
import br.eti.arthurgregorio.shiroee.config.jdbc.UserDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * The user representation
 *
 * @author Arthur Gregorio
 *
 * @version 3.0.0
 * @since 1.0.0, 06/10/2013
 */
@Entity
@Audited
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", schema = CONFIGURATION)
@AuditTable(value = "users", schema = CONFIGURATION_AUDIT)
public class User extends PersistentEntity implements UserDetails {

    @Getter
    @Setter
    @NotNull(message = "{user.name}")
    @Column(name = "name", length = 90, nullable = false)
    private String name;
    @Getter
    @Setter
    @NotNull(message = "{user.email}")
    @Column(name = "email", length = 90, nullable = false)
    private String email;
    @Getter
    @Setter
    @NotNull(message = "{user.username}")
    @Column(name = "username", length = 20, nullable = false)
    private String username;
    @Getter
    @Setter
    @Column(name = "password", length = 60)
    private String password;
    @Getter
    @Setter
    @Column(name = "active", nullable = false)
    private boolean active;

    @Getter
    @Setter
    @JoinColumn(name = "id_profile", nullable = false)
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Profile profile;
    @Getter
    @Setter
    @ManyToOne
    @NotNull(message = "{user.group}")
    @JoinColumn(name = "id_group", nullable = false)
    private Group group;
    
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @NotNull(message = "{user.store-type}")
    @Column(name = "store_type", nullable = false)
    private StoreType storeType;
    
    @Getter
    @Setter
    @Transient
    private String passwordConfirmation;

    /**
     * Constructor
     */
    public User() {
        this.active = true;
        this.profile = new Profile();
        this.storeType = StoreType.LOCAL;
    }

    /**
     * To indicate if the user is blocked or not
     *
     * @return true for blocked accounts false otherwise
     */
    @Override
    public boolean isBlocked() {
        return !this.isActive();
    }
/**
     * Method to check if this user is a bind account used only to host the basic data enable the LDAP/AD authentication
     * process
     *
     * @return true or false
     */
    @Override
    public boolean isLdapBindAccount() {
        return this.storeType == StoreType.LDAP;
    }

    /**
     * To get this user permissions
     * 
     * @return the {@link String} {@link Set} of permissions
     */
    @Override
    public Set<String> getPermissions() {
        return this.group != null ? this.group.getPermissions() : new HashSet<>();
    }

    /**
     * To get the name from this user {@link Group}
     *
     * @return the name of the liked {@link Group}
     */
    public String getGroupName() {
        return this.group != null ? this.group.getName() : null;
    }
    
    /**
     * To check if the password of this user is valid in the password change process
     * 
     * @return true or false
     */
    public boolean isPasswordValid() {
        return isNotBlank(this.password) 
                && isNotBlank(this.passwordConfirmation) 
                && this.password.equals(this.passwordConfirmation);
    }

    /**
     * To check if the password of this user has changed
     * 
     * @return true or false
     */
    public boolean hasChangedPasswords() {
        return isNotBlank(this.password) 
                && isNotBlank(this.passwordConfirmation);
    }
    
    /**
     * To check if this user is the default administrator
     * 
     * @return true or false
     */
    public boolean isAdministrator() {
        return this.username.equals("admin");
    }
}
