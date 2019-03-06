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
package br.com.webbudget.domain.entities.configuration;

import br.com.webbudget.domain.entities.PersistentEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static br.com.webbudget.infrastructure.utils.DefaultSchemes.*;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.FetchType.EAGER;

/**
 * This class represents a group of {@link Authorization} for a collection of {@link User}
 *
 * @author Arthur Gregorio
 *
 * @version 1.0.0
 * @since 2.0.0, 26/05/2015
 */
@Entity
@Audited
@ToString(exclude = "grants")
@Table(name = "groups", schema = CONFIGURATION)
@AuditTable(value = "groups", schema = CONFIGURATION_AUDIT)
@EqualsAndHashCode(callSuper = true, exclude = "grants")
public class Group extends PersistentEntity {

    @Getter
    @Setter
    @NotNull(message = "{group.name}")
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Getter
    @Setter
    @Column(name = "active", nullable = false)
    private boolean active;
    
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Group parent;
    
    @OneToMany(mappedBy = "group", fetch = EAGER, cascade = {REMOVE})
    private final List<Grant> grants;
    
    /**
     * Constructor
     */
    public Group() { 
        this.active = true;
        this.grants = Collections.emptyList();
    }

    /**
     * Constructor
     * 
     * @param name the name of the group
     */
    public Group(String name) {
        this();
        this.name = name;
    }
    
    /**
     * Constructor
     *
     * @param name the name of the group
     * @param parent the parent group of this group
     */
    public Group(String name, Group parent) {
        this();
        this.name = name;
        this.parent = parent;
    }
    
    /**
     * Return the {@link Grant} of this group and inherit the {@link Grant} of the parent group
     *
     * @return list of this group {@link Grant}
     */
    public List<Grant> getGrants() {
        
        final List<Grant> groupGrants = new ArrayList<>(this.grants);
        
        if (this.parent != null) {
            groupGrants.addAll(this.parent.getGrants());
        }
        return Collections.unmodifiableList(groupGrants);
    }

    /**
     * Same as the {@link #getGrants()} but this one return the already processed permission {@link String} instead of
     * the {@link Grant}. This is a helper method.
     *
     * @return a {@link String} {@link Set} of the permissions
     */
    public Set<String> getPermissions() {
        return this.getGrants().stream()
                    .map(Grant::getAuthorization)
                    .map(Authorization::getFullPermission)
                    .collect(Collectors.toSet());
    }

    /**
     * Method used to check if this group is the default admin
     *
     * @return true or false
     */
    public boolean isAdministratorsGroup() {
        return this.name.equals("Administradores");
    }

    /**
     * Method to check if this is the default administrators group
     *
     * @return true if is and false otherwise
     */
    public boolean isAdministrator() {
        return this.name.equals("Administradores");
    }
}
