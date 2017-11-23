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
package br.com.webbudget.domain.model.security;

import br.com.webbudget.domain.model.entity.PersistentEntity;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Arthur Gregorio
 *
 * @version 2.0.0
 * @since 2.0.0, 26/05/2015
 */
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "groups", schema = "security")
public class Group extends PersistentEntity {

    @Getter
    @Setter
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Getter
    @Setter
    @Column(name = "blocked", nullable = false)
    private boolean blocked;
    
    @Getter
    @Setter
    private Group parent;
    
    private final List<Role> roles;
    
    /**
     * 
     */
    public Group() { 
        this.blocked = false;
        this.roles = Collections.emptyList();
    }

    /**
     * 
     * @param name 
     */
    public Group(String name) {
        this();
        this.name = name;
    }

    /**
     * 
     * @param name
     * @param parent 
     */
    public Group(String name, Group parent) {
        this();
        this.name = name;
        this.parent = parent;
    }
    
    /**
     * 
     * @param role 
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }
    
    /**
     * 
     * @param roles 
     */
    public void addRoles(List<Role> roles) {
        this.roles.addAll(roles);
    }

    /**
     * 
     * @return 
     */
    public List<Role> getRoles() {
        return Collections.unmodifiableList(this.roles);
    }
}
