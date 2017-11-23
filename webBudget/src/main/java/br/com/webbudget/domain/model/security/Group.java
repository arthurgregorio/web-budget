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
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REMOVE;
import javax.persistence.Column;
import javax.persistence.Entity;
import static javax.persistence.FetchType.EAGER;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @ManyToOne
    @JoinColumn(name = "id_parent")
    private Group parent;
    
    @OneToMany(mappedBy = "group", fetch = EAGER, cascade = {PERSIST, MERGE, REMOVE})
    private final List<Grant> grants;
    
    /**
     * 
     */
    public Group() { 
        this.blocked = false;
        this.grants = Collections.emptyList();
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
     * @param grant 
     */
    public void addRole(Grant grant) {
        this.grants.add(grant);
    }
    
    /**
     * 
     * @param grants 
     */
    public void addRoles(List<Grant> grants) {
        this.grants.addAll(grants);
    }

    /**
     * 
     * @return 
     */
    public List<Grant> getGrants() {
        return Collections.unmodifiableList(this.grants);
    }
}
